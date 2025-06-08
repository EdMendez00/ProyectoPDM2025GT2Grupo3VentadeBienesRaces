from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import PropertyViewSet, PropertyVisitViewSet

router = DefaultRouter()
router.register('properties', PropertyViewSet, basename='property')
router.register('visits', PropertyVisitViewSet, basename='property-visit')

urlpatterns = [
    path('', include(router.urls)),
]
