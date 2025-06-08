package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.RetrofitClient
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.models.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para manejar la autenticación con el backend de Django.
 * Permite iniciar sesión, almacenar y recuperar el token de autenticación.
 */
class AuthRepository(private val context: Context) {

    companion object {
        private const val AUTH_PREFS = "auth_preferences"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val TAG = "AuthRepository"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE)

    /**
     * Inicia sesión en el backend de Django usando el email y la contraseña del usuario.
     * @param email Email del usuario.
     * @param password Contraseña del usuario.
     * @return true si la autenticación fue exitosa, false en caso contrario.
     */
    suspend fun loginToDjango(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val loginRequest = LoginRequest(username = email, password = password)
            val response = RetrofitClient.apiService.login(loginRequest)

            if (response.isSuccessful && response.body() != null) {
                // Guardar el token en SharedPreferences
                val token = response.body()!!.token
                saveAuthToken(token)

                // Configurar el token en RetrofitClient para futuras peticiones
                RetrofitClient.setAuthToken(token)

                return@withContext true
            } else {
                Log.e(TAG, "Error en login: ${response.errorBody()?.string()}")
                return@withContext false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción en login: ${e.message}", e)
            return@withContext false
        }
    }

    /**
     * Guarda el token de autenticación en las preferencias.
     * @param token Token de autenticación a guardar.
     */
    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    /**
     * Obtiene el token de autenticación guardado.
     * @return El token guardado o null si no existe.
     */
    fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    /**
     * Configura el token guardado en RetrofitClient si existe.
     * Este método debe llamarse al inicio de la aplicación.
     */
    fun setupStoredToken() {
        val storedToken = getAuthToken()
        if (storedToken != null) {
            RetrofitClient.setAuthToken(storedToken)
        }
    }

    /**
     * Cierra la sesión eliminando el token guardado.
     */
    fun logout() {
        sharedPreferences.edit().remove(KEY_AUTH_TOKEN).apply()
    }
}
