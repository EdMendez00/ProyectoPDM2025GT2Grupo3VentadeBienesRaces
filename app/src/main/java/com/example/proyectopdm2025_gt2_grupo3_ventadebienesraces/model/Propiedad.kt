package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Propiedad(
    @DocumentId
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val ubicacion: Ubicacion = Ubicacion(),
    val dimensiones: Dimensiones = Dimensiones(),
    val caracteristicas: List<String> = listOf(),
    val imagenes: List<String> = listOf(), // URLs de Firebase Storage
    val vendedorId: String = "",
    val fechaPublicacion: Timestamp = Timestamp.now(),
    val estado: String = "DISPONIBLE", // "DISPONIBLE" o "VENDIDA"
    val medioContacto: String = ""
)

data class Ubicacion(
    val direccion: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)

data class Dimensiones(
    val largo: Double = 0.0,
    val ancho: Double = 0.0,
    val area: Double = 0.0 // En metros cuadrados
)
