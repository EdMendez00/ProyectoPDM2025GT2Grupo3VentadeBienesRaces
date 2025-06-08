package com.example.proyectopdm.network

import com.example.proyectopdm.models.*
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {
    @POST("users/register/")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @POST("users/login/")
    suspend fun loginUser(@Body authRequest: AuthRequest): Response<AuthResponse>

    @GET("users/profile/")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<User>

    @PUT("users/profile/")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body user: User
    ): Response<User>

    @POST("users/logout/")
    suspend fun logout(@Header("Authorization") token: String): Response<Map<String, String>>
}
