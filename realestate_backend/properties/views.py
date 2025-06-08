from django.shortcuts import render, get_object_or_404
from rest_framework import viewsets, permissions, status, filters
from rest_framework.response import Response
from rest_framework.decorators import action
from rest_framework.parsers import MultiPartParser, FormParser
from django.db.models import Q
from django.utils import timezone
from .models import Property, PropertyImage, PropertyVisit
from .serializers.property_serializers import (
    PropertyListSerializer, PropertyDetailSerializer, PropertyCreateUpdateSerializer,
    PropertyImageSerializer, PropertyVisitSerializer
)

class IsOwnerOrReadOnly(permissions.BasePermission):
    """
    Permiso personalizado para permitir que solo los propietarios de un objeto lo editen.
    """
    def has_object_permission(self, request, view, obj):
        # Los permisos de lectura están permitidos para cualquier solicitud
        if request.method in permissions.SAFE_METHODS:
            return True
        # Los permisos de escritura solo están permitidos para el propietario
        return obj.owner == request.user


class IsAdminUser(permissions.BasePermission):
    """
    Permiso que solo permite el acceso a usuarios administradores.
    """
    def has_permission(self, request, view):
        return bool(request.user and request.user.is_staff)


class PropertyViewSet(viewsets.ModelViewSet):
    """
    ViewSet para las propiedades inmobiliarias.
    """
    filter_backends = [filters.SearchFilter, filters.OrderingFilter]
    search_fields = ['title', 'description', 'address', 'city', 'state']
    ordering_fields = ['price', 'created_at', 'area']
    ordering = ['-created_at']

    def get_queryset(self):
        queryset = Property.objects.all()

        # Filtrar por estado si se especifica en los query params
        status_param = self.request.query_params.get('status', None)
        if status_param:
            queryset = queryset.filter(status=status_param.upper())
        else:
            # Por defecto, excluir las propiedades vendidas
            queryset = queryset.exclude(status='VENDIDA')

        # Filtrar por rango de precios
        min_price = self.request.query_params.get('min_price', None)
        max_price = self.request.query_params.get('max_price', None)

        if min_price:
            queryset = queryset.filter(price__gte=min_price)
        if max_price:
            queryset = queryset.filter(price__lte=max_price)

        # Filtrar por ubicación
        city = self.request.query_params.get('city', None)
        if city:
            queryset = queryset.filter(city__icontains=city)

        # Filtrar por usuario propietario
        owner_id = self.request.query_params.get('owner_id', None)
        if owner_id:
            queryset = queryset.filter(owner_id=owner_id)

        return queryset

    def get_serializer_class(self):
        if self.action == 'list':
            return PropertyListSerializer
        elif self.action in ['create', 'update', 'partial_update']:
            return PropertyCreateUpdateSerializer
        return PropertyDetailSerializer

    def get_permissions(self):
        if self.action in ['create']:
            # Solo usuarios autenticados pueden crear propiedades
            permission_classes = [permissions.IsAuthenticated]
        elif self.action in ['update', 'partial_update', 'destroy']:
            # Solo el propietario o un admin puede editar o eliminar
            permission_classes = [permissions.IsAuthenticated, IsOwnerOrReadOnly]
        elif self.action == 'admin_delete_old':
            # Solo administradores pueden eliminar propiedades antiguas
            permission_classes = [IsAdminUser]
        else:
            # Cualquiera puede ver propiedades
            permission_classes = [permissions.AllowAny]
        return [permission() for permission in permission_classes]

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)

    @action(detail=True, methods=['post'], parser_classes=[MultiPartParser, FormParser])
    def upload_images(self, request, pk=None):
        property = self.get_object()

        # Verificar que el usuario sea el propietario
        if property.owner != request.user:
            return Response({"detail": "No tienes permiso para subir imágenes a esta propiedad."},
                            status=status.HTTP_403_FORBIDDEN)

        # Verificar cuántas imágenes tiene ya la propiedad
        existing_images_count = property.images.count()
        if existing_images_count >= 5:
            return Response({"detail": "Esta propiedad ya tiene el máximo de 5 imágenes."},
                           status=status.HTTP_400_BAD_REQUEST)

        # Procesar las imágenes subidas
        images = request.FILES.getlist('images')
        if len(images) > (5 - existing_images_count):
            return Response({"detail": f"Solo puedes subir {5 - existing_images_count} imágenes más."},
                           status=status.HTTP_400_BAD_REQUEST)

        # Marcar la primera imagen como principal si no hay otras
        is_first = existing_images_count == 0

        result = []
        for image in images:
            property_image = PropertyImage.objects.create(
                property=property,
                image=image,
                is_main=is_first
            )
            is_first = False  # Solo la primera será principal
            result.append(PropertyImageSerializer(property_image, context={'request': request}).data)

        return Response(result, status=status.HTTP_201_CREATED)

    @action(detail=True, methods=['post'], url_path='set-main-image')
    def set_main_image(self, request, pk=None):
        property = self.get_object()

        # Verificar que el usuario sea el propietario
        if property.owner != request.user:
            return Response({"detail": "No tienes permiso para modificar esta propiedad."},
                            status=status.HTTP_403_FORBIDDEN)

        image_id = request.data.get('image_id')
        if not image_id:
            return Response({"detail": "Debes proporcionar el ID de la imagen."},
                           status=status.HTTP_400_BAD_REQUEST)

        try:
            # Quitar is_main de todas las imágenes de la propiedad
            property.images.update(is_main=False)

            # Marcar la imagen seleccionada como principal
            image = PropertyImage.objects.get(id=image_id, property=property)
            image.is_main = True
            image.save()

            return Response({"detail": "Imagen principal actualizada correctamente."},
                           status=status.HTTP_200_OK)
        except PropertyImage.DoesNotExist:
            return Response({"detail": "La imagen no existe o no pertenece a esta propiedad."},
                          status=status.HTTP_404_NOT_FOUND)

    @action(detail=True, methods=['delete'], url_path='delete-image/(?P<image_id>[^/.]+)')
    def delete_image(self, request, pk=None, image_id=None):
        property = self.get_object()

        # Verificar que el usuario sea el propietario
        if property.owner != request.user:
            return Response({"detail": "No tienes permiso para eliminar imágenes de esta propiedad."},
                            status=status.HTTP_403_FORBIDDEN)

        try:
            image = PropertyImage.objects.get(id=image_id, property=property)
            was_main = image.is_main
            image.delete()

            # Si la imagen borrada era la principal, asignar otra como principal
            if was_main:
                first_image = property.images.first()
                if first_image:
                    first_image.is_main = True
                    first_image.save()

            return Response({"detail": "Imagen eliminada correctamente."},
                           status=status.HTTP_204_NO_CONTENT)
        except PropertyImage.DoesNotExist:
            return Response({"detail": "La imagen no existe o no pertenece a esta propiedad."},
                          status=status.HTTP_404_NOT_FOUND)

    @action(detail=False, methods=['delete'], url_path='admin-delete-old')
    def admin_delete_old(self, request):
        """
        Acción para que los administradores eliminen propiedades VENDIDAS muy antiguas.
        """
        # Obtener días desde el parámetro o usar valor predeterminado de 180 días (6 meses)
        days = int(request.query_params.get('days', 180))
        cutoff_date = timezone.now() - timezone.timedelta(days=days)

        # Filtrar propiedades vendidas antes de la fecha de corte
        old_properties = Property.objects.filter(
            status='VENDIDA',
            sold_at__lt=cutoff_date
        )

        count = old_properties.count()
        old_properties.delete()

        return Response({
            "detail": f"Se han eliminado {count} propiedades vendidas hace más de {days} días."
        }, status=status.HTTP_200_OK)

    @action(detail=True, methods=['patch'], url_path='mark-as-sold')
    def mark_as_sold(self, request, pk=None):
        property = self.get_object()

        # Verificar que el usuario sea el propietario
        if property.owner != request.user:
            return Response({"detail": "No tienes permiso para modificar esta propiedad."},
                            status=status.HTTP_403_FORBIDDEN)

        property.status = 'VENDIDA'
        property.sold_at = timezone.now()
        property.save()

        serializer = PropertyDetailSerializer(property, context={'request': request})
        return Response(serializer.data)


class PropertyVisitViewSet(viewsets.ModelViewSet):
    """
    ViewSet para las visitas a propiedades.
    """
    serializer_class = PropertyVisitSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        user = self.request.user

        # Los propietarios ven las visitas a sus propiedades
        if self.request.query_params.get('as_owner') == 'true' and user.is_seller:
            return PropertyVisit.objects.filter(property__owner=user)

        # Por defecto, los usuarios ven sus propias visitas
        return PropertyVisit.objects.filter(visitor=user)

    def perform_create(self, serializer):
        property_id = self.request.data.get('property')
        property_obj = get_object_or_404(Property, id=property_id)

        # Verificar que la propiedad esté disponible
        if property_obj.status == 'VENDIDA':
            from rest_framework import serializers
            raise serializers.ValidationError("No se pueden agendar visitas a propiedades ya vendidas.")

        serializer.save(visitor=self.request.user)

    @action(detail=True, methods=['patch'], url_path='update-status')
    def update_status(self, request, pk=None):
        visit = self.get_object()
        status_value = request.data.get('status')

        # Verificar que el status sea válido
        if status_value not in dict(PropertyVisit.STATUS_CHOICES):
            return Response({"detail": "Estado no válido."},
                           status=status.HTTP_400_BAD_REQUEST)

        # Solo el propietario puede cambiar el estado (excepto a CANCELADA que puede el visitante)
        if visit.property.owner != request.user and (status_value != 'CANCELADA' or visit.visitor != request.user):
            return Response({"detail": "No tienes permiso para cambiar el estado de esta visita."},
                           status=status.HTTP_403_FORBIDDEN)

        visit.status = status_value
        visit.save()

        serializer = self.get_serializer(visit)
        return Response(serializer.data)
