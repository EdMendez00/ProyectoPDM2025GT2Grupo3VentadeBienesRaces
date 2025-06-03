package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Visita(
    @DocumentId
    val id: String = "",
    val propiedadId: String = "",
    val clienteId: String = "",
    val vendedorId: String = "",
    val fecha: Timestamp = Timestamp.now(),
    val hora: String = "",
    val estado: String = "PENDIENTE", // "PENDIENTE", "CONFIRMADA", "REALIZADA", "CANCELADA"
    val comentarios: String = ""
)
