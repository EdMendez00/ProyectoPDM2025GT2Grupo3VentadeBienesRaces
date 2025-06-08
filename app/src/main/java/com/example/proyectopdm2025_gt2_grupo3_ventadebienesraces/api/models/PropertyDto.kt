package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.models

import com.google.gson.annotations.SerializedName

data class PropertyDto(
    val id: Long? = null,
    val title: String,
    val description: String?,
    val price: Double,
    val address: String?,
    val city: String?,
    val state: String?,
    val width: Double?,
    val length: Double?,
    val area: Double?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val parking: Int?,
    val status: String = "DISPONIBLE",
    @SerializedName("contact_phone")
    val contactPhone: String?,
    @SerializedName("contact_email")
    val contactEmail: String?
)

data class PropertyResponseDto(
    val id: Long,
    val title: String,
    val slug: String,
    val description: String?,
    val price: Double,
    val address: String?,
    val city: String?,
    val state: String?,
    val width: Double?,
    val length: Double?,
    val area: Double?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val parking: Int?,
    val status: String,
    @SerializedName("contact_phone")
    val contactPhone: String?,
    @SerializedName("contact_email")
    val contactEmail: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("sold_at")
    val soldAt: String?
)
