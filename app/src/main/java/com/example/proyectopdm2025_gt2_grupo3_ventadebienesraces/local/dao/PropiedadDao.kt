package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.PropiedadEntity

@Dao
interface PropiedadDao {
    @Query("SELECT * FROM propiedades WHERE estado = 'DISPONIBLE' ORDER BY fechaPublicacion DESC")
    fun getPropiedadesDisponibles(): LiveData<List<PropiedadEntity>>

    @Query("SELECT * FROM propiedades WHERE estado = 'VENDIDA' ORDER BY fechaPublicacion DESC")
    fun getPropiedadesVendidas(): LiveData<List<PropiedadEntity>>

    @Query("SELECT * FROM propiedades WHERE vendedorId = :vendedorId ORDER BY fechaPublicacion DESC")
    fun getPropiedadesByVendedor(vendedorId: String): LiveData<List<PropiedadEntity>>

    @Query("SELECT * FROM propiedades WHERE id = :propiedadId")
    fun getPropiedadById(propiedadId: Long): LiveData<PropiedadEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropiedad(propiedad: PropiedadEntity): Long

    @Update
    suspend fun updatePropiedad(propiedad: PropiedadEntity)

    @Delete
    suspend fun deletePropiedad(propiedad: PropiedadEntity)

    @Query("UPDATE propiedades SET estado = 'VENDIDA' WHERE id = :propiedadId")
    suspend fun marcarComoVendida(propiedadId: Long)

    @Query("SELECT * FROM propiedades WHERE titulo LIKE '%' || :query || '%' OR descripcion LIKE '%' || :query || '%'")
    fun searchPropiedades(query: String): LiveData<List<PropiedadEntity>>

    @Query("SELECT * FROM propiedades WHERE precio BETWEEN :minPrecio AND :maxPrecio AND estado = 'DISPONIBLE'")
    fun getPropiedadesByPrecio(minPrecio: Double, maxPrecio: Double): LiveData<List<PropiedadEntity>>
}
