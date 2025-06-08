package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "propiedades")
data class PropiedadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val largo: Double,
    val ancho: Double,
    val area: Double,
    val tipoPropiedad: String, // Añadido: tipo de propiedad (Casa, Apartamento, etc.)
    val dormitorios: Int, // Añadido: número de dormitorios
    val banos: Int, // Añadido: número de baños
    val caracteristicas: String,  // Lista separada por comas
    val vendedorId: String,
    val fechaPublicacion: Date,
    val estado: String,  // "DISPONIBLE" o "VENDIDA"
    val medioContacto: String
)
