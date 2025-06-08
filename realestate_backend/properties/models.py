from django.db import models
from django.conf import settings
from django.utils.text import slugify
from django.utils import timezone

class Property(models.Model):
    """
    Modelo para las propiedades inmobiliarias en venta.
    """
    STATUS_CHOICES = [
        ('DISPONIBLE', 'Disponible'),
        ('VISITANDO', 'En proceso de visitas'),
        ('VENDIDA', 'Vendida'),
    ]

    title = models.CharField('Título', max_length=200)
    slug = models.SlugField('Slug', max_length=250, unique=True, blank=True)
    description = models.TextField('Descripción')
    price = models.DecimalField('Precio', max_digits=12, decimal_places=2)
    address = models.CharField('Dirección', max_length=255)
    city = models.CharField('Ciudad', max_length=100)
    state = models.CharField('Departamento', max_length=100)
    width = models.DecimalField('Ancho (m)', max_digits=8, decimal_places=2)
    length = models.DecimalField('Largo (m)', max_digits=8, decimal_places=2)
    area = models.DecimalField('Área (m²)', max_digits=10, decimal_places=2)
    bedrooms = models.PositiveSmallIntegerField('Dormitorios', default=0)
    bathrooms = models.PositiveSmallIntegerField('Baños', default=0)
    parking = models.PositiveSmallIntegerField('Estacionamientos', default=0)
    status = models.CharField('Estado', max_length=20, choices=STATUS_CHOICES, default='DISPONIBLE')

    owner = models.ForeignKey(
        settings.AUTH_USER_MODEL,
        on_delete=models.CASCADE,
        related_name='properties',
        verbose_name='Propietario'
    )

    contact_phone = models.CharField('Teléfono de contacto', max_length=15, blank=True)
    contact_email = models.EmailField('Email de contacto', blank=True)

    created_at = models.DateTimeField('Fecha de creación', auto_now_add=True)
    updated_at = models.DateTimeField('Última actualización', auto_now=True)
    sold_at = models.DateTimeField('Fecha de venta', null=True, blank=True)

    def save(self, *args, **kwargs):
        if not self.slug:
            self.slug = slugify(self.title)

        # Si el estado cambia a VENDIDA, registrar la fecha de venta
        if self.status == 'VENDIDA' and not self.sold_at:
            self.sold_at = timezone.now()

        super().save(*args, **kwargs)

    def __str__(self):
        return f"{self.title} - {self.get_status_display()}"

    class Meta:
        verbose_name = 'Propiedad'
        verbose_name_plural = 'Propiedades'
        ordering = ['-created_at']


class PropertyImage(models.Model):
    """
    Modelo para las imágenes de las propiedades.
    Cada propiedad puede tener hasta 5 imágenes.
    """
    property = models.ForeignKey(
        Property,
        on_delete=models.CASCADE,
        related_name='images',
        verbose_name='Propiedad'
    )
    image = models.ImageField('Imagen', upload_to='property_images/')
    is_main = models.BooleanField('Es imagen principal', default=False)
    created_at = models.DateTimeField('Fecha de creación', auto_now_add=True)

    def __str__(self):
        return f"Imagen de {self.property.title}"

    class Meta:
        verbose_name = 'Imagen de propiedad'
        verbose_name_plural = 'Imágenes de propiedades'
        ordering = ['property', '-is_main']


class PropertyVisit(models.Model):
    """
    Modelo para las visitas programadas a las propiedades.
    """
    STATUS_CHOICES = [
        ('PENDIENTE', 'Pendiente de confirmación'),
        ('CONFIRMADA', 'Visita confirmada'),
        ('CANCELADA', 'Visita cancelada'),
        ('COMPLETADA', 'Visita completada'),
    ]

    property = models.ForeignKey(
        Property,
        on_delete=models.CASCADE,
        related_name='visits',
        verbose_name='Propiedad'
    )
    visitor = models.ForeignKey(
        settings.AUTH_USER_MODEL,
        on_delete=models.CASCADE,
        related_name='property_visits',
        verbose_name='Visitante'
    )

    visit_date = models.DateField('Fecha de visita')
    visit_time = models.TimeField('Hora de visita')
    notes = models.TextField('Notas adicionales', blank=True)
    status = models.CharField('Estado', max_length=20, choices=STATUS_CHOICES, default='PENDIENTE')

    created_at = models.DateTimeField('Fecha de creación', auto_now_add=True)
    updated_at = models.DateTimeField('Última actualización', auto_now=True)

    def __str__(self):
        return f"Visita de {self.visitor} a {self.property.title}"

    class Meta:
        verbose_name = 'Visita'
        verbose_name_plural = 'Visitas'
        ordering = ['visit_date', 'visit_time']
