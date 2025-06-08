package com.example.proyectopdm.repository

import com.example.proyectopdm.models.Property
import com.example.proyectopdm.models.PropertyImage
import com.example.proyectopdm.models.PropertyVisit
import com.example.proyectopdm.network.ApiConfig
import com.example.proyectopdm.network.PropertyApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

class PropertyRepository {
    private val propertyApiService = ApiConfig.createService<PropertyApiService>()

    suspend fun getAllProperties(
        token: String? = null,
        status: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        city: String? = null,
        ownerId: Int? = null
    ): Response<List<Property>> {
        return propertyApiService.getAllProperties(
            token?.let { "Token $it" },
            status, minPrice, maxPrice, city, ownerId
        )
    }

    suspend fun getPropertyById(id: Int, token: String? = null): Response<Property> {
        return propertyApiService.getPropertyById(id, token?.let { "Token $it" })
    }

    suspend fun createProperty(token: String, property: Property): Response<Property> {
        return propertyApiService.createProperty("Token $token", property)
    }

    suspend fun updateProperty(id: Int, token: String, property: Property): Response<Property> {
        return propertyApiService.updateProperty(id, "Token $token", property)
    }

    suspend fun deleteProperty(id: Int, token: String): Response<Void> {
        return propertyApiService.deleteProperty(id, "Token $token")
    }

    suspend fun markPropertyAsSold(id: Int, token: String): Response<Property> {
        return propertyApiService.markPropertyAsSold(id, "Token $token")
    }

    // Upload images to a property
    suspend fun uploadPropertyImages(propertyId: Int, token: String, imageFiles: List<File>): Response<List<PropertyImage>> {
        val parts = imageFiles.map { file ->
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, requestBody)
        }
        return propertyApiService.uploadPropertyImages(propertyId, "Token $token", parts)
    }

    suspend fun setMainImage(propertyId: Int, token: String, imageId: Int): Response<Map<String, String>> {
        val body = mapOf("image_id" to imageId)
        return propertyApiService.setMainImage(propertyId, "Token $token", body)
    }

    suspend fun deletePropertyImage(propertyId: Int, imageId: Int, token: String): Response<Map<String, String>> {
        return propertyApiService.deletePropertyImage(propertyId, imageId, "Token $token")
    }

    // Visits
    suspend fun getUserVisits(token: String, asOwner: Boolean = false): Response<List<PropertyVisit>> {
        return propertyApiService.getUserVisits("Token $token", asOwner)
    }

    suspend fun scheduleVisit(token: String, visit: PropertyVisit): Response<PropertyVisit> {
        return propertyApiService.scheduleVisit("Token $token", visit)
    }

    suspend fun updateVisitStatus(visitId: Int, token: String, status: String): Response<PropertyVisit> {
        return propertyApiService.updateVisitStatus(visitId, "Token $token", mapOf("status" to status))
    }

    suspend fun cancelVisit(visitId: Int, token: String): Response<Void> {
        return propertyApiService.cancelVisit(visitId, "Token $token")
    }
}
