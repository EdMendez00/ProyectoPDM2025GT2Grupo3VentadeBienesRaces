package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api

import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.models.LoginRequest
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.models.PropertyDto
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.models.PropertyResponseDto
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.models.TokenResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Autenticación
    @POST("users/login/")
    suspend fun login(@Body loginRequest: LoginRequest): Response<TokenResponse>

    @GET("properties/")
    suspend fun getProperties(): Response<List<PropertyResponseDto>>

    @GET("properties/{id}/")
    suspend fun getPropertyById(@Path("id") id: Long): Response<PropertyResponseDto>

    @POST("properties/")
    suspend fun createProperty(@Body property: PropertyDto): Response<PropertyResponseDto>

    @PUT("properties/{id}/")
    suspend fun updateProperty(@Path("id") id: Long, @Body property: PropertyDto): Response<PropertyResponseDto>

    @DELETE("properties/{id}/")
    suspend fun deleteProperty(@Path("id") id: Long): Response<Void>

    // Subir imágenes para una propiedad
    @Multipart
    @POST("properties/{id}/upload_images/")
    suspend fun uploadPropertyImage(
        @Path("id") propertyId: Long,
        @Part image: MultipartBody.Part,
        @Part("is_main") isMain: Boolean = false
    ): Response<Any>
}
