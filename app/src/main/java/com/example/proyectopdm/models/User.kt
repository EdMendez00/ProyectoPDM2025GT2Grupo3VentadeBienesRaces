package com.example.proyectopdm.models

data class User(
    val id: Int? = null,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone_number: String? = null,
    val is_seller: Boolean = false,
    val bio: String? = null,
    val profile_photo: String? = null
)

data class AuthRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val password2: String,
    val first_name: String,
    val last_name: String,
    val phone_number: String? = null,
    val is_seller: Boolean = false
)

data class AuthResponse(
    val token: String,
    val user_id: Int,
    val email: String,
    val is_seller: Boolean,
    val first_name: String,
    val last_name: String
)
