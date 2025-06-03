package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.UsuarioEntity

@Entity(
    tableName = "propiedades",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["vendedorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("vendedorId")
    ]
)
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
    val caracteristicas: String,  // Características separadas por comas
    val vendedorId: String,
    val fechaPublicacion: Date,
    val estado: String, // "DISPONIBLE" o "VENDIDA"
    val medioContacto: String
)
