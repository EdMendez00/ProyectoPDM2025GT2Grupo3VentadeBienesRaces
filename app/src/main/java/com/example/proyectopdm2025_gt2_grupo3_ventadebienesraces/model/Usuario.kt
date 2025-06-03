package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Usuario(
    @DocumentId
    val id: String = "",
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val rol: String = "", // "cliente", "vendedor" o "admin"
    val fechaRegistro: Timestamp = Timestamp.now()
)
