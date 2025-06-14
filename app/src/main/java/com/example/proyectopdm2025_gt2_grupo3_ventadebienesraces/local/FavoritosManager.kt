package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Gestor simplificado de propiedades favoritas.
 * Guarda toda la lista como un único JSON en SharedPreferences.
 */
class FavoritosManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson: Gson = GsonBuilder().setLenient().create()
    private val listType: Type = object : TypeToken<List<Propiedad>>() {}.type

    // Lista de listeners para notificar sobre cambios en favoritos
    private val favoritoChangeListeners = mutableListOf<FavoritoChangeListener>()

    init {
        val count = getFavoritos().size
        Log.d(TAG, "FavoritosManager inicializado con $count favoritos")
    }

    /**
     * Interfaz para recibir notificaciones de cambios en favoritos
     */
    interface FavoritoChangeListener {
        fun onFavoritoChanged(propiedadId: String, esFavorito: Boolean)
    }

    /**
     * Registra un listener para recibir notificaciones de cambios en favoritos
     */
    fun addFavoritoChangeListener(listener: FavoritoChangeListener) {
        if (!favoritoChangeListeners.contains(listener)) {
            favoritoChangeListeners.add(listener)
            Log.d(TAG, "Listener añadido. Total: ${favoritoChangeListeners.size}")
        }
    }

    /**
     * Elimina un listener previamente registrado
     */
    fun removeFavoritoChangeListener(listener: FavoritoChangeListener) {
        favoritoChangeListeners.remove(listener)
        Log.d(TAG, "Listener eliminado. Total: ${favoritoChangeListeners.size}")
    }

    /**
     * Notifica a todos los listeners registrados sobre un cambio en el estado de favorito
     */
    private fun notifyFavoritoChanged(propiedadId: String, esFavorito: Boolean) {
        Log.d(TAG, "Notificando cambio en favorito: $propiedadId, esFavorito=$esFavorito")
        // Copia la lista para evitar problemas si se modifica durante la iteración
        val listeners = ArrayList(favoritoChangeListeners)
        listeners.forEach { listener ->
            try {
                listener.onFavoritoChanged(propiedadId, esFavorito)
            } catch (e: Exception) {
                Log.e(TAG, "Error al notificar listener: ${e.message}")
            }
        }
    }

    /**
     * Obtiene la lista completa de propiedades favoritas.
     */
    fun getFavoritos(): MutableList<Propiedad> {
        val json = prefs.getString(KEY_FAVORITOS_LIST, "[]")
        val favoritos = try {
            gson.fromJson<MutableList<Propiedad>>(json, listType) ?: mutableListOf()
        } catch (e: Exception) {
            Log.e(TAG, "Error al deserializar favoritos: ${e.message}")
            mutableListOf<Propiedad>()
        }

        // Asegurarse de que todos estén marcados como favoritos
        favoritos.forEach { it.esFavorito = true }

        return favoritos
    }

    /**
     * Guarda la lista completa de favoritos.
     */
    private fun saveFavoritos(favoritos: List<Propiedad>): Boolean {
        return try {
            val json = gson.toJson(favoritos)
            prefs.edit().putString(KEY_FAVORITOS_LIST, json).commit()
            Log.d(TAG, "Favoritos guardados: ${favoritos.size} elementos")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar favoritos: ${e.message}")
            false
        }
    }

    /**
     * Verifica si una propiedad está marcada como favorita.
     */
    fun esFavorito(propiedadId: String): Boolean {
        val favoritos = getFavoritos()
        return favoritos.any { it.id == propiedadId }
    }

    /**
     * Marca una propiedad como favorita.
     */
    fun agregarFavorito(propiedad: Propiedad): Boolean {
        val favoritos = getFavoritos()

        // Verificar si ya existe
        if (favoritos.any { it.id == propiedad.id }) {
            Log.d(TAG, "La propiedad ${propiedad.id} ya está en favoritos")
            return true
        }

        // Marcar como favorito
        propiedad.esFavorito = true
        favoritos.add(propiedad)

        val resultado = saveFavoritos(favoritos)
        Log.d(TAG, "Propiedad ${propiedad.id} agregada a favoritos: $resultado")

        // Notificar el cambio a los listeners
        if (resultado) {
            notifyFavoritoChanged(propiedad.id, true)
        }

        return resultado
    }

    /**
     * Elimina una propiedad de favoritos.
     */
    fun eliminarFavorito(propiedadId: String): Boolean {
        val favoritos = getFavoritos()
        val tamanioOriginal = favoritos.size

        favoritos.removeAll { it.id == propiedadId }

        if (favoritos.size == tamanioOriginal) {
            Log.d(TAG, "Propiedad $propiedadId no encontrada en favoritos")
            return false
        }

        val resultado = saveFavoritos(favoritos)
        Log.d(TAG, "Propiedad $propiedadId eliminada de favoritos: $resultado")

        // Notificar el cambio a los listeners
        if (resultado) {
            notifyFavoritoChanged(propiedadId, false)
        }

        return resultado
    }

    /**
     * Cambia el estado de favorito de una propiedad.
     * @return true si ahora es favorito, false si ya no lo es
     */
    fun toggleFavorito(propiedad: Propiedad): Boolean {
        return if (esFavorito(propiedad.id)) {
            eliminarFavorito(propiedad.id)
            false
        } else {
            agregarFavorito(propiedad)
            true
        }
    }

    /**
     * Limpia todos los favoritos.
     */
    fun limpiarTodosFavoritos() {
        val favoritos = getFavoritos()
        val ids = favoritos.map { it.id }

        prefs.edit().clear().commit()
        Log.d(TAG, "Todos los favoritos han sido eliminados")

        // Notificar que todos los favoritos han sido eliminados
        ids.forEach { propiedadId ->
            notifyFavoritoChanged(propiedadId, false)
        }
    }

    companion object {
        private const val TAG = "FavoritosManager"
        private const val PREF_NAME = "favoritos_prefs_v2"
        private const val KEY_FAVORITOS_LIST = "favoritos_list"

        // Instancia singleton para uso en toda la app
        @Volatile
        private var INSTANCE: FavoritosManager? = null

        fun getInstance(context: Context): FavoritosManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoritosManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
