package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.mappers

import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.models.PropertyDto
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.models.PropertyResponseDto
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.PropiedadEntity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Convierte una entidad PropiedadEntity de Room a un DTO PropertyDto para enviar a la API
 */
fun PropiedadEntity.toPropertyDto(): PropertyDto {
    // Extraer ciudad y departamento de la dirección completa
    val direccionParts = this.direccion.split(", ")
    val ciudad = if (direccionParts.size > 1) direccionParts[1] else ""
    val departamento = if (direccionParts.size > 2) direccionParts[2] else ""

    return PropertyDto(
        id = this.id,
        title = this.titulo,
        description = this.descripcion,
        price = this.precio,
        address = this.direccion,
        city = ciudad,
        state = departamento,
        width = this.largo,
        length = this.ancho,
        area = this.area,
        bedrooms = 0, // No tenemos esta información en la entidad local
        bathrooms = 0, // No tenemos esta información en la entidad local
        parking = 0,  // No tenemos esta información en la entidad local
        status = this.estado,
        contactPhone = this.medioContacto.substringAfter("Teléfono: ").substringBefore(","),
        contactEmail = this.medioContacto.substringAfter("Email: ").substringBefore(",")
    )
}

/**
 * Convierte un DTO PropertyResponseDto recibido de la API a una entidad PropiedadEntity para Room
 */
fun PropertyResponseDto.toPropiedadEntity(vendedorId: String): PropiedadEntity {
    // Formatear la dirección completa
    val direccionCompleta = "$address, $city, $state"

    // Crear el objeto de fecha
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val fechaPublicacion = try {
        dateFormat.parse(this.createdAt)
    } catch (e: Exception) {
        Date()
    }

    return PropiedadEntity(
        id = this.id,
        titulo = this.title,
        descripcion = this.description ?: "",
        precio = this.price,
        direccion = direccionCompleta,
        latitud = 0.0,
        longitud = 0.0,
        largo = this.width ?: 0.0,
        ancho = this.length ?: 0.0,
        area = this.area ?: 0.0,
        caracteristicas = "",
        vendedorId = vendedorId,
        fechaPublicacion = fechaPublicacion,
        estado = this.status,
        medioContacto = "Teléfono: ${this.contactPhone ?: ""}, Email: ${this.contactEmail ?: ""}"
    )
}
