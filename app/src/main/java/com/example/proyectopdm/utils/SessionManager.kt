package com.example.proyectopdm.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.proyectopdm.models.AuthResponse

/**
 * Clase para gestionar la sesión del usuario
 */
class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = prefs.edit()

    companion object {
        const val PREF_NAME = "RealEstateAppPrefs"
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
        const val USER_FIRST_NAME = "user_first_name"
        const val USER_LAST_NAME = "user_last_name"
        const val IS_SELLER = "is_seller"
    }

    /**
     * Guardar datos de sesión del usuario
     */
    fun saveAuthData(authResponse: AuthResponse) {
        editor.putString(USER_TOKEN, authResponse.token)
        editor.putInt(USER_ID, authResponse.user_id)
        editor.putString(USER_EMAIL, authResponse.email)
        editor.putString(USER_FIRST_NAME, authResponse.first_name)
        editor.putString(USER_LAST_NAME, authResponse.last_name)
        editor.putBoolean(IS_SELLER, authResponse.is_seller)
        editor.apply()
    }

    /**
     * Verificar si el usuario está logueado
     */
    fun isLoggedIn(): Boolean {
        return prefs.getString(USER_TOKEN, null) != null
    }

    /**
     * Obtener token de usuario
     */
    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Obtener ID de usuario
     */
    fun getUserId(): Int {
        return prefs.getInt(USER_ID, -1)
    }

    /**
     * Obtener email de usuario
     */
    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    /**
     * Obtener nombre completo del usuario
     */
    fun getUserFullName(): String {
        val firstName = prefs.getString(USER_FIRST_NAME, "") ?: ""
        val lastName = prefs.getString(USER_LAST_NAME, "") ?: ""
        return "$firstName $lastName"
    }

    /**
     * Verificar si es vendedor
     */
    fun isSeller(): Boolean {
        return prefs.getBoolean(IS_SELLER, false)
    }

    /**
     * Cerrar sesión
     */
    fun logout() {
        editor.clear()
        editor.apply()
    }
}
