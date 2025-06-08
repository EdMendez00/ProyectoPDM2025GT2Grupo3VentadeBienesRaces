package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/api/"  // Para el emulador Android, apunta a localhost del host
    // Si pruebas en dispositivo físico, usa la dirección IP de tu ordenador
    // private const val BASE_URL = "http://192.168.1.x:8000/api/"

    private var token: String? = null

    fun setAuthToken(newToken: String) {
        token = newToken
    }

    private val gson: Gson by lazy {
        GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
    }

    private val httpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val originalRequest = chain.request()

                // Si hay un token disponible, añadirlo a todas las peticiones
                val request = if (token != null) {
                    originalRequest.newBuilder()
                        .header("Authorization", "Token $token")
                        .build()
                } else {
                    originalRequest
                }

                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
