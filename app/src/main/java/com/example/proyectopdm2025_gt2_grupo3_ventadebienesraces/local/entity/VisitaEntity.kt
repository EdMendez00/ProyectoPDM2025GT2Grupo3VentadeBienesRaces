package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.PropiedadEntity
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.UsuarioEntity

@Entity(
    tableName = "visitas",
    foreignKeys = [
        ForeignKey(
            entity = PropiedadEntity::class,
            parentColumns = ["id"],
            childColumns = ["propiedadId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["vendedorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("propiedadId"),
        Index("clienteId"),
        Index("vendedorId")
    ]
)
data class VisitaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propiedadId: Long,
    val clienteId: String,
    val vendedorId: String,
    val fecha: Date,
    val hora: String,
    val estado: String, // "PENDIENTE", "CONFIRMADA", "REALIZADA", "CANCELADA"
    val comentarios: String
)
