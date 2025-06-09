package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException

/**
 * Gestor de propiedades favoritas que utiliza SharedPreferences para almacenamiento local.
 */
class FavoritosManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson: Gson = GsonBuilder().setLenient().create()

    init {
        // Registrar el número de favoritos al inicializar (para depuración)
        val favoritos = getFavoritosIds()
        Log.d(TAG, "Inicializando FavoritosManager: ${favoritos.size} favoritos encontrados")
        for (id in favoritos) {
            Log.d(TAG, "ID favorito: $id")
        }
    }

    /**
     * Verifica si una propiedad está marcada como favorita.
     */
    fun esFavorito(propiedadId: String): Boolean {
        val favoritosSet = getFavoritosIds()
        val esFav = favoritosSet.contains(propiedadId)
        Log.d(TAG, "Verificando favorito $propiedadId: $esFav")
        return esFav
    }

    /**
     * Marca una propiedad como favorita.
     */
    fun marcarComoFavorito(propiedad: Propiedad): Boolean {
        Log.d(TAG, "Marcando como favorito: ${propiedad.id}")
        val favoritosSet = getFavoritosIds().toMutableSet()
        favoritosSet.add(propiedad.id)

        // Guardar en SharedPreferences
        val saved = prefs.edit()
            .putStringSet(KEY_FAVORITOS, favoritosSet)
            .commit()

        // Guardar la información completa de la propiedad
        guardarPropiedadFavorita(propiedad)

        return saved
    }

    /**
     * Desmarca una propiedad como favorita.
     */
    fun desmarcarComoFavorito(propiedadId: String): Boolean {
        Log.d(TAG, "Desmarcando favorito: $propiedadId")
        val favoritosSet = getFavoritosIds().toMutableSet()

        // Verificar si la propiedad está en favoritos
        if (!favoritosSet.contains(propiedadId)) {
            Log.d(TAG, "La propiedad $propiedadId no estaba en favoritos")
            return true
        }

        // Remover del conjunto de IDs
        favoritosSet.remove(propiedadId)

        // Guardar el conjunto actualizado
        val savedSet = prefs.edit()
            .putStringSet(KEY_FAVORITOS, favoritosSet)
            .commit()

        // Eliminar datos de la propiedad
        val removedData = eliminarPropiedadFavorita(propiedadId)

        Log.d(TAG, "Favorito eliminado ($propiedadId): ID Set=$savedSet, Data=$removedData")
        return savedSet && removedData
    }

    /**
     * Marca o desmarca una propiedad como favorita.
     * @return true si la propiedad fue marcada como favorita, false si fue desmarcada
     */
    fun toggleFavorito(propiedad: Propiedad): Boolean {
        val favoritosSet = getFavoritosIds().toMutableSet()

        return if (favoritosSet.contains(propiedad.id)) {
            // Está en favoritos, hay que desmarcar
            desmarcarComoFavorito(propiedad.id)
            false // Fue desmarcada
        } else {
            // No está en favoritos, hay que marcar
            marcarComoFavorito(propiedad)
            true // Fue marcada
        }
    }

    /**
     * Obtiene todos los IDs de las propiedades favoritas.
     */
    fun getFavoritosIds(): Set<String> {
        val resultado = prefs.getStringSet(KEY_FAVORITOS, setOf()) ?: setOf()
        Log.d(TAG, "Obteniendo IDs de favoritos: ${resultado.size} elementos")
        return resultado
    }

    /**
     * Guarda la información completa de una propiedad marcada como favorita.
     */
    fun guardarPropiedadFavorita(propiedad: Propiedad): Boolean {
        try {
            val json = gson.toJson(propiedad)
            val key = KEY_PROPIEDAD_PREFIX + propiedad.id
            val result = prefs.edit().putString(key, json).commit()
            Log.d(TAG, "Guardando datos de propiedad favorita ${propiedad.id}: $result")
            return result
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar propiedad favorita: ${e.message}", e)
            return false
        }
    }

    /**
     * Elimina la información de una propiedad que ya no es favorita.
     */
    fun eliminarPropiedadFavorita(propiedadId: String): Boolean {
        val key = KEY_PROPIEDAD_PREFIX + propiedadId
        val result = prefs.edit().remove(key).commit()
        Log.d(TAG, "Eliminando datos de propiedad: $propiedadId - Resultado: $result")
        return result
    }

    /**
     * Obtiene la lista completa de propiedades favoritas.
     */
    fun getPropiedadesFavoritas(): List<Propiedad> {
        val propiedades = mutableListOf<Propiedad>()
        val favoritosIds = getFavoritosIds()

        Log.d(TAG, "Recuperando ${favoritosIds.size} propiedades favoritas")

        for (id in favoritosIds) {
            val key = KEY_PROPIEDAD_PREFIX + id
            val json = prefs.getString(key, null)

            if (json != null) {
                try {
                    val propiedad = gson.fromJson(json, Propiedad::class.java)
                    if (propiedad != null) {
                        propiedad.esFavorito = true
                        propiedades.add(propiedad)
                        Log.d(TAG, "Propiedad favorita recuperada: ${propiedad.id}")
                    } else {
                        Log.w(TAG, "La deserialización devolvió null para $id")
                        limpiarFavoritoInvalido(id)
                    }
                } catch (e: JsonSyntaxException) {
                    Log.e(TAG, "Error al deserializar propiedad: $id", e)
                    limpiarFavoritoInvalido(id)
                } catch (e: Exception) {
                    Log.e(TAG, "Error desconocido al cargar propiedad: $id", e)
                    limpiarFavoritoInvalido(id)
                }
            } else {
                Log.w(TAG, "No hay datos JSON para la propiedad favorita: $id")
                limpiarFavoritoInvalido(id)
            }
        }

        Log.d(TAG, "Total de propiedades favoritas recuperadas: ${propiedades.size}")
        return propiedades
    }

    /**
     * Limpia un favorito inválido de todas las preferencias
     */
    private fun limpiarFavoritoInvalido(id: String) {
        Log.d(TAG, "Limpiando favorito inválido: $id")
        // Eliminar del conjunto de IDs
        val favoritosSet = getFavoritosIds().toMutableSet()
        favoritosSet.remove(id)
        prefs.edit().putStringSet(KEY_FAVORITOS, favoritosSet).commit()

        // Eliminar datos de la propiedad
        eliminarPropiedadFavorita(id)
    }

    /**
     * Limpia todos los favoritos (para pruebas)
     */
    fun limpiarTodosFavoritos() {
        prefs.edit().clear().commit()
        Log.d(TAG, "Todos los favoritos han sido eliminados")
    }

    companion object {
        private const val TAG = "FavoritosManager"
        private const val PREF_NAME = "favoritos_prefs"
        private const val KEY_FAVORITOS = "favoritos_ids"
        private const val KEY_PROPIEDAD_PREFIX = "propiedad_"
    }
}
