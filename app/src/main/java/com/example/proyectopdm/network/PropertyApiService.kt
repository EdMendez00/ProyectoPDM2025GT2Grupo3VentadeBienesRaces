package com.example.proyectopdm.network

import com.example.proyectopdm.models.Property
import com.example.proyectopdm.models.PropertyImage
import com.example.proyectopdm.models.PropertyVisit
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PropertyApiService {
    // Endpoints para propiedades
    @GET("properties/properties/")
    suspend fun getAllProperties(
        @Header("Authorization") token: String? = null,
        @Query("status") status: String? = null,
        @Query("min_price") minPrice: Double? = null,
        @Query("max_price") maxPrice: Double? = null,
        @Query("city") city: String? = null,
        @Query("owner_id") ownerId: Int? = null
    ): Response<List<Property>>

    @GET("properties/properties/{id}/")
    suspend fun getPropertyById(
        @Path("id") id: Int,
        @Header("Authorization") token: String? = null
    ): Response<Property>

    @POST("properties/properties/")
    suspend fun createProperty(
        @Header("Authorization") token: String,
        @Body property: Property
    ): Response<Property>

    @PUT("properties/properties/{id}/")
    suspend fun updateProperty(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body property: Property
    ): Response<Property>

    @DELETE("properties/properties/{id}/")
    suspend fun deleteProperty(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Void>

    @PATCH("properties/properties/{id}/mark-as-sold/")
    suspend fun markPropertyAsSold(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Property>

    // Endpoints para imágenes de propiedades
    @Multipart
    @POST("properties/properties/{id}/upload-images/")
    suspend fun uploadPropertyImages(
        @Path("id") propertyId: Int,
        @Header("Authorization") token: String,
        @Part images: List<MultipartBody.Part>
    ): Response<List<PropertyImage>>

    @POST("properties/properties/{id}/set-main-image/")
    suspend fun setMainImage(
        @Path("id") propertyId: Int,
        @Header("Authorization") token: String,
        @Body body: Map<String, Int>
    ): Response<Map<String, String>>

    @DELETE("properties/properties/{id}/delete-image/{imageId}/")
    suspend fun deletePropertyImage(
        @Path("id") propertyId: Int,
        @Path("imageId") imageId: Int,
        @Header("Authorization") token: String
    ): Response<Map<String, String>>

    // Endpoints para visitas a propiedades
    @GET("properties/visits/")
    suspend fun getUserVisits(
        @Header("Authorization") token: String,
        @Query("as_owner") asOwner: Boolean = false
    ): Response<List<PropertyVisit>>

    @POST("properties/visits/")
    suspend fun scheduleVisit(
        @Header("Authorization") token: String,
        @Body visit: PropertyVisit
    ): Response<PropertyVisit>

    @PATCH("properties/visits/{id}/update-status/")
    suspend fun updateVisitStatus(
        @Path("id") visitId: Int,
        @Header("Authorization") token: String,
        @Body status: Map<String, String>
    ): Response<PropertyVisit>

    @DELETE("properties/visits/{id}/")
    suspend fun cancelVisit(
        @Path("id") visitId: Int,
        @Header("Authorization") token: String
    ): Response<Void>
}
