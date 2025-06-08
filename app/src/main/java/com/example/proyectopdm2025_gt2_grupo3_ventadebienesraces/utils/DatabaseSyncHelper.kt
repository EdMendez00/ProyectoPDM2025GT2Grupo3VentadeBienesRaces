package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.utils

import android.content.Context
import android.util.Log
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao.UsuarioDao
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.UsuarioEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

/**
 * Clase de utilidad para manejar la sincronización entre el backend y la base de datos local
 */
class DatabaseSyncHelper(
    private val context: Context,
    private val usuarioDao: UsuarioDao
) {

    /**
     * Asegura que el usuario exista en la base de datos local antes de crear propiedades
     * @param userId ID del usuario (vendedor)
     * @param nombre Nombre del usuario
     * @param correo Correo electrónico del usuario
     * @param telefono Teléfono del usuario
     * @return true si el usuario existe o fue creado correctamente
     */
    suspend fun asegurarUsuarioExiste(
        userId: String,
        nombre: String,
        correo: String,
        telefono: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            // Verificar si el usuario ya existe en la base de datos local
            Log.d("DatabaseSync", "Verificando usuario con ID: $userId")
            val usuarioExistente = usuarioDao.obtenerUsuarioPorId(userId)

            if (usuarioExistente != null) {
                Log.d("DatabaseSync", "Usuario encontrado en la BD local")
                return@withContext true
            }

            // Si no existe, crear el usuario en la base de datos local
            Log.d("DatabaseSync", "Usuario no encontrado, creando nuevo registro")
            val nuevoUsuario = UsuarioEntity(
                id = userId,
                nombre = nombre,
                correo = correo,
                telefono = telefono,
                rol = "vendedor", // Por defecto asumimos que es vendedor
                fechaRegistro = Date()
            )

            // Insertar el nuevo usuario
            usuarioDao.insertUsuario(nuevoUsuario)
            Log.d("DatabaseSync", "Usuario creado exitosamente")

            return@withContext true
        } catch (e: Exception) {
            Log.e("DatabaseSync", "Error al sincronizar usuario: ${e.message}")
            return@withContext false
        }
    }
}
