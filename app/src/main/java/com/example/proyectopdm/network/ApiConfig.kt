package com.example.proyectopdm.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Configuración para la conexión API al backend de Django
 */
object ApiConfig {
    // URL base del servidor - cámbiala según donde esté desplegado tu servidor
    private const val BASE_URL = "http://10.0.2.2:8000/api/" // Para el emulador Android apuntando a localhost
    // private const val BASE_URL = "http://tu-ip-local:8000/api/" // Para dispositivos físicos en la misma red
    // private const val BASE_URL = "https://tu-dominio.com/api/" // Para producción

    // Cliente HTTP con interceptor para logs
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    // Instancia de Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Función para obtener servicios API tipados
    inline fun <reified T> createService(): T {
        return retrofit.create(T::class.java)
    }
}
