package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.Date

@IgnoreExtraProperties
data class Propiedad(
    @DocumentId
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val ubicacion: Ubicacion = Ubicacion(),
    val dimensiones: Dimensiones = Dimensiones(),
    val caracteristicas: List<String> = listOf(),
    val imagenes: List<String> = listOf(),
    val estado: String = "DISPONIBLE",
    val medioContacto: String = "",
    val vendedorId: String = "",
    val fechaPublicacion: Date = Date(),
    val dormitorios: Int = 0,
    val banos: Int = 0,
    val tipoPropiedad: String = ""
)

@IgnoreExtraProperties
data class Ubicacion(
    val direccion: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)

@IgnoreExtraProperties
data class Dimensiones(
    val largo: Double = 0.0,
    val ancho: Double = 0.0,
    val area: Double = 0.0
)
