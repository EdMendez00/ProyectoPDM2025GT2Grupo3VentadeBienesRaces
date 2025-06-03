package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val rol: String, // "cliente", "vendedor" o "admin"
    val fechaRegistro: Date
)
