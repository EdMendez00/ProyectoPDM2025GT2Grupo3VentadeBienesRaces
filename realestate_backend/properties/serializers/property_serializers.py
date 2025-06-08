from rest_framework import serializers
from properties.models import Property, PropertyImage, PropertyVisit
from django.contrib.auth import get_user_model

User = get_user_model()

class PropertyImageSerializer(serializers.ModelSerializer):
    """
    Serializador para las imágenes de propiedades.
    """
    class Meta:
        model = PropertyImage
        fields = ['id', 'image', 'is_main', 'created_at']
        read_only_fields = ['id', 'created_at']


class PropertyListSerializer(serializers.ModelSerializer):
    """
    Serializador para listado de propiedades con información básica.
    """
    owner_name = serializers.SerializerMethodField()
    main_image = serializers.SerializerMethodField()

    class Meta:
        model = Property
        fields = ['id', 'title', 'slug', 'price', 'city', 'state', 'area',
                 'bedrooms', 'bathrooms', 'status', 'created_at', 'owner_name', 'main_image']
        read_only_fields = ['id', 'slug', 'created_at']

    def get_owner_name(self, obj):
        return f"{obj.owner.first_name} {obj.owner.last_name}"

    def get_main_image(self, obj):
        main_image = obj.images.filter(is_main=True).first()
        if not main_image:
            main_image = obj.images.first()

        if main_image:
            return self.context['request'].build_absolute_uri(main_image.image.url)
        return None


class PropertyDetailSerializer(serializers.ModelSerializer):
    """
    Serializador para detalle completo de una propiedad.
    """
    images = PropertyImageSerializer(many=True, read_only=True)
    owner_details = serializers.SerializerMethodField()

    class Meta:
        model = Property
        fields = ['id', 'title', 'slug', 'description', 'price', 'address', 'city', 'state',
                 'width', 'length', 'area', 'bedrooms', 'bathrooms', 'parking',
                 'status', 'owner_details', 'contact_phone', 'contact_email',
                 'created_at', 'updated_at', 'sold_at', 'images']
        read_only_fields = ['id', 'slug', 'created_at', 'updated_at', 'sold_at']

    def get_owner_details(self, obj):
        return {
            'id': obj.owner.id,
            'name': f"{obj.owner.first_name} {obj.owner.last_name}",
            'email': obj.owner.email,
            'phone': obj.owner.phone_number,
            'profile_photo': self.context['request'].build_absolute_uri(obj.owner.profile_photo.url) if obj.owner.profile_photo else None
        }


class PropertyCreateUpdateSerializer(serializers.ModelSerializer):
    """
    Serializador para crear y actualizar propiedades.
    """
    class Meta:
        model = Property
        fields = ['title', 'description', 'price', 'address', 'city', 'state',
                 'width', 'length', 'area', 'bedrooms', 'bathrooms', 'parking',
                 'contact_phone', 'contact_email']

    def validate(self, attrs):
        # Calcular el área si no se proporcionó
        if 'width' in attrs and 'length' in attrs and 'area' not in attrs:
            attrs['area'] = attrs['width'] * attrs['length']
        return attrs


class PropertyVisitSerializer(serializers.ModelSerializer):
    """
    Serializador para las visitas a propiedades.
    """
    visitor_name = serializers.SerializerMethodField(read_only=True)
    property_title = serializers.SerializerMethodField(read_only=True)

    class Meta:
        model = PropertyVisit
        fields = ['id', 'property', 'property_title', 'visitor', 'visitor_name',
                 'visit_date', 'visit_time', 'notes', 'status',
                 'created_at', 'updated_at']
        read_only_fields = ['id', 'created_at', 'updated_at']

    def get_visitor_name(self, obj):
        return f"{obj.visitor.first_name} {obj.visitor.last_name}"

    def get_property_title(self, obj):
        return obj.property.title
