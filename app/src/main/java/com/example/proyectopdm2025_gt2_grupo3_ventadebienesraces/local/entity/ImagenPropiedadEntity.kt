package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "imagenes_propiedad",
    foreignKeys = [
        ForeignKey(
            entity = PropiedadEntity::class,
            parentColumns = ["id"],
            childColumns = ["propiedadId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("propiedadId")]
)
data class ImagenPropiedadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propiedadId: Long,
    val rutaImagen: String
)
