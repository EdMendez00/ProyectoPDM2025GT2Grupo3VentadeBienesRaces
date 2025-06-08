package com.example.proyectopdm.repository

import com.example.proyectopdm.models.AuthRequest
import com.example.proyectopdm.models.AuthResponse
import com.example.proyectopdm.models.RegisterRequest
import com.example.proyectopdm.models.User
import com.example.proyectopdm.network.ApiConfig
import com.example.proyectopdm.network.UserApiService
import retrofit2.Response

class UserRepository {
    private val userApiService = ApiConfig.createService<UserApiService>()

    suspend fun registerUser(registerRequest: RegisterRequest): Response<AuthResponse> {
        return userApiService.registerUser(registerRequest)
    }

    suspend fun loginUser(email: String, password: String): Response<AuthResponse> {
        val authRequest = AuthRequest(email, password)
        return userApiService.loginUser(authRequest)
    }

    suspend fun getUserProfile(token: String): Response<User> {
        return userApiService.getUserProfile("Token $token")
    }

    suspend fun updateUserProfile(token: String, user: User): Response<User> {
        return userApiService.updateUserProfile("Token $token", user)
    }

    suspend fun logout(token: String): Response<Map<String, String>> {
        return userApiService.logout("Token $token")
    }
}
