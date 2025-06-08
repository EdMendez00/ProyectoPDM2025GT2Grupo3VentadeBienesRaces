package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.viewmodel

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.RetrofitClient
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.api.mappers.toPropertyDto
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.AppDatabase
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao.ImagenPropiedadDao
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao.PropiedadDao
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao.UsuarioDao
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.ImagenPropiedadEntity
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.PropiedadEntity
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.UsuarioEntity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Date

class PropiedadViewModel(application: Application) : AndroidViewModel(application) {

    private val propiedadDao: PropiedadDao
    private val imagenPropiedadDao: ImagenPropiedadDao
    private val usuarioDao: UsuarioDao
    private val apiService = RetrofitClient.apiService

    init {
        val database = AppDatabase.getDatabase(application)
        propiedadDao = database.propiedadDao()
        imagenPropiedadDao = database.imagenPropiedadDao()
        usuarioDao = database.usuarioDao()
    }

    // Propiedades disponibles
    fun getPropiedadesDisponibles(): LiveData<List<PropiedadEntity>> {
        return propiedadDao.getPropiedadesDisponibles()
    }

    // Propiedades vendidas
    fun getPropiedadesVendidas(): LiveData<List<PropiedadEntity>> {
        return propiedadDao.getPropiedadesVendidas()
    }

    // Propiedades de un vendedor específico
    fun getPropiedadesByVendedor(vendedorId: String): LiveData<List<PropiedadEntity>> {
        return propiedadDao.getPropiedadesByVendedor(vendedorId)
    }

    // Detalles de una propiedad
    fun getPropiedadById(propiedadId: Long): LiveData<PropiedadEntity> {
        return propiedadDao.getPropiedadById(propiedadId)
    }

    // Insertar una nueva propiedad y devolver su ID
    fun insertPropiedad(propiedad: PropiedadEntity, callback: (Long) -> Unit) {
        viewModelScope.launch {
            try {
                // Primero insertamos en la base de datos local
                val propiedadId = propiedadDao.insertPropiedad(propiedad)

                // También enviamos al servidor backend si hay conexión a internet
                syncPropiedadWithBackend(propiedad, propiedadId)

                callback(propiedadId)
            } catch (e: Exception) {
                Log.e("PropiedadViewModel", "Error al insertar propiedad: ${e.message}")
                callback(-1) // Retornar -1 en caso de error
            }
        }
    }

    // Insertar una imagen de propiedad
    fun insertImagen(imagen: ImagenPropiedadEntity) {
        viewModelScope.launch {
            try {
                // Guardar la imagen en la base de datos local
                imagenPropiedadDao.insertImagen(imagen)

                // Sincronizar la imagen con el backend de Django
                syncImagenWithBackend(imagen)
            } catch (e: Exception) {
                Log.e("PropiedadViewModel", "Error al insertar imagen: ${e.message}")
            }
        }
    }

    // Método para sincronizar la propiedad con el backend de Django
    private suspend fun syncPropiedadWithBackend(propiedad: PropiedadEntity, propiedadId: Long) {
        try {
            // Solo sincronizar si hay conexión a internet
            if (!isNetworkAvailable()) {
                Log.d("PropiedadViewModel", "No hay conexión a internet, sincronización pendiente")
                return
            }

            // Convertir la entidad local a un DTO para la API
            val propertyDto = propiedad.toPropertyDto()

            // Enviar al servidor usando Retrofit
            val response = apiService.createProperty(propertyDto)

            if (response.isSuccessful) {
                Log.d("PropiedadViewModel", "Propiedad sincronizada con éxito en el backend de Django. ID: ${response.body()?.id}")
            } else {
                Log.e("PropiedadViewModel", "Error en sincronización: ${response.code()} - ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("PropiedadViewModel", "Error al sincronizar con backend: ${e.message}")
            // La falla en la sincronización no debería afectar al flujo principal
            // ya que la propiedad ya se guardó localmente
        }
    }

    // Método para sincronizar una imagen con el backend de Django
    private suspend fun syncImagenWithBackend(imagen: ImagenPropiedadEntity) {
        try {
            // Solo sincronizar si hay conexión a internet
            if (!isNetworkAvailable()) {
                Log.d("PropiedadViewModel", "No hay conexión a internet, sincronización de imagen pendiente")
                return
            }

            // Verificar que el archivo exista
            val imageFile = File(imagen.rutaImagen)
            if (!imageFile.exists()) {
                Log.e("PropiedadViewModel", "El archivo de imagen no existe: ${imagen.rutaImagen}")
                return
            }

            // Preparar la imagen para envío multipart
            val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            // Subir la imagen al servidor
            val response = apiService.uploadPropertyImage(
                propertyId = imagen.propiedadId,
                image = body,
                isMain = true // La primera imagen se marca como principal
            )

            if (response.isSuccessful) {
                Log.d("PropiedadViewModel", "Imagen sincronizada con éxito en el backend de Django")
            } else {
                Log.e("PropiedadViewModel", "Error al sincronizar imagen: ${response.code()} - ${response.errorBody()?.string()}")
            }

        } catch (e: Exception) {
            Log.e("PropiedadViewModel", "Error al sincronizar imagen con backend: ${e.message}")
        }
    }

    // Comprueba si hay conexión a internet disponible
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getApplication<Application>()
            .getSystemService(ConnectivityManager::class.java)

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
