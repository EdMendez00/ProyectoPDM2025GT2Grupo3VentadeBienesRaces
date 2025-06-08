from django.contrib import admin
from .models import Property, PropertyImage, PropertyVisit

class PropertyImageInline(admin.TabularInline):
    model = PropertyImage
    extra = 1
    max_num = 5
    readonly_fields = ['created_at']

class PropertyVisitInline(admin.TabularInline):
    model = PropertyVisit
    extra = 0
    readonly_fields = ['created_at', 'updated_at']

@admin.register(Property)
class PropertyAdmin(admin.ModelAdmin):
    list_display = ('title', 'owner', 'price', 'city', 'status', 'created_at')
    list_filter = ('status', 'city', 'state')
    search_fields = ('title', 'description', 'address', 'owner__email')
    prepopulated_fields = {'slug': ('title',)}
    readonly_fields = ['created_at', 'updated_at', 'sold_at']
    inlines = [PropertyImageInline, PropertyVisitInline]

    fieldsets = (
        ('Información básica', {
            'fields': ('title', 'slug', 'description', 'price', 'owner')
        }),
        ('Ubicación', {
            'fields': ('address', 'city', 'state')
        }),
        ('Características', {
            'fields': ('width', 'length', 'area', 'bedrooms', 'bathrooms', 'parking')
        }),
        ('Contacto', {
            'fields': ('contact_phone', 'contact_email')
        }),
        ('Estado y fechas', {
            'fields': ('status', 'created_at', 'updated_at', 'sold_at')
        }),
    )

@admin.register(PropertyVisit)
class PropertyVisitAdmin(admin.ModelAdmin):
    list_display = ('property', 'visitor', 'visit_date', 'visit_time', 'status')
    list_filter = ('status', 'visit_date')
    search_fields = ('property__title', 'visitor__email', 'notes')
    readonly_fields = ['created_at', 'updated_at']

    fieldsets = (
        ('Información de visita', {
            'fields': ('property', 'visitor', 'visit_date', 'visit_time', 'status')
        }),
        ('Detalles adicionales', {
            'fields': ('notes', 'created_at', 'updated_at')
        }),
    )
