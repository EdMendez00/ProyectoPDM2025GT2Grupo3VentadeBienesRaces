package com.example.proyectopdm.models

import java.util.Date

data class Property(
    val id: Int? = null,
    val title: String,
    val slug: String? = null,
    val description: String? = null,
    val price: Double,
    val address: String? = null,
    val city: String,
    val state: String,
    val width: Double? = null,
    val length: Double? = null,
    val area: Double,
    val bedrooms: Int? = 0,
    val bathrooms: Int? = 0,
    val parking: Int? = 0,
    val status: String = "DISPONIBLE",
    val contact_phone: String? = null,
    val contact_email: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val sold_at: String? = null,
    val main_image: String? = null,
    val owner_name: String? = null,
    val owner_details: OwnerDetails? = null,
    val images: List<PropertyImage>? = null
)

data class OwnerDetails(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String? = null,
    val profile_photo: String? = null
)

data class PropertyImage(
    val id: Int? = null,
    val image: String,
    val is_main: Boolean = false,
    val created_at: String? = null
)

data class PropertyVisit(
    val id: Int? = null,
    val property: Int,
    val property_title: String? = null,
    val visitor: Int? = null,
    val visitor_name: String? = null,
    val visit_date: String,
    val visit_time: String,
    val notes: String? = null,
    val status: String = "PENDIENTE",
    val created_at: String? = null,
    val updated_at: String? = null
)
