from django.shortcuts import render
from django.contrib.auth import get_user_model
from rest_framework import generics, permissions, status
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework.authtoken.models import Token
from rest_framework.authtoken.views import ObtainAuthToken
from .serializers.user_serializers import (
    UserSerializer, UserRegisterSerializer, PasswordChangeSerializer
)

User = get_user_model()

class UserRegistrationView(generics.CreateAPIView):
    """
    Vista para registrar nuevos usuarios.
    """
    queryset = User.objects.all()
    permission_classes = [permissions.AllowAny]
    serializer_class = UserRegisterSerializer

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        user = serializer.save()
        token, created = Token.objects.get_or_create(user=user)
        return Response({
            "user": UserSerializer(user, context=self.get_serializer_context()).data,
            "token": token.key
        }, status=status.HTTP_201_CREATED)


class CustomAuthToken(ObtainAuthToken):
    """
    Vista personalizada para obtener token de autenticación.
    """
    def post(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data,
                                           context={'request': request})
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data['user']
        token, created = Token.objects.get_or_create(user=user)
        return Response({
            'token': token.key,
            'user_id': user.pk,
            'email': user.email,
            'is_seller': user.is_seller,
            'first_name': user.first_name,
            'last_name': user.last_name
        })


class UserDetailView(generics.RetrieveUpdateAPIView):
    """
    Vista para ver y actualizar el perfil del usuario autenticado.
    """
    serializer_class = UserSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_object(self):
        return self.request.user


class ChangePasswordView(generics.UpdateAPIView):
    """
    Vista para cambiar la contraseña del usuario autenticado.
    """
    serializer_class = PasswordChangeSerializer
    permission_classes = [permissions.IsAuthenticated]

    def update(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        # Verificar la contraseña actual
        user = request.user
        if not user.check_password(serializer.data.get("old_password")):
            return Response({"old_password": ["Contraseña incorrecta."]},
                           status=status.HTTP_400_BAD_REQUEST)

        # Establecer la nueva contraseña
        user.set_password(serializer.data.get("new_password"))
        user.save()

        # Actualizar el token de autenticación
        Token.objects.filter(user=user).delete()
        token = Token.objects.create(user=user)

        return Response({
            "message": "Contraseña actualizada correctamente.",
            "token": token.key
        }, status=status.HTTP_200_OK)


class LogoutView(APIView):
    """
    Vista para cerrar sesión (eliminar el token).
    """
    permission_classes = [permissions.IsAuthenticated]

    def post(self, request):
        # Eliminar el token del usuario
        request.user.auth_token.delete()
        return Response({"message": "Sesión cerrada correctamente."},
                       status=status.HTTP_200_OK)
