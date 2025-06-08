package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.models

// Modelo para la solicitud de autenticación a Django
data class LoginRequest(
    val username: String,  // Usualmente el email del usuario
    val password: String
)

// Modelo para la respuesta de autenticación de Django
data class TokenResponse(
    val token: String
)
