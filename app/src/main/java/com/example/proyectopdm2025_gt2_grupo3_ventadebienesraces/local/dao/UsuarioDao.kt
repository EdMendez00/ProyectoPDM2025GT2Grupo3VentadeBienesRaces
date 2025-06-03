package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.UsuarioEntity

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios")
    fun getAllUsuarios(): LiveData<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun getUsuarioById(id: String): LiveData<UsuarioEntity>

    @Query("SELECT * FROM usuarios WHERE rol = :rol")
    fun getUsuariosByRol(rol: String): LiveData<List<UsuarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: UsuarioEntity)

    @Update
    suspend fun updateUsuario(usuario: UsuarioEntity)

    @Delete
    suspend fun deleteUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun getUsuarioByCorreo(correo: String): UsuarioEntity?
}
