package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.ImagenPropiedadEntity

@Dao
interface ImagenPropiedadDao {
    @Query("SELECT * FROM imagenes_propiedad WHERE propiedadId = :propiedadId")
    fun getImagenesByPropiedad(propiedadId: Long): LiveData<List<ImagenPropiedadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImagen(imagen: ImagenPropiedadEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImagenes(imagenes: List<ImagenPropiedadEntity>)

    @Delete
    suspend fun deleteImagen(imagen: ImagenPropiedadEntity)

    @Query("DELETE FROM imagenes_propiedad WHERE propiedadId = :propiedadId")
    suspend fun deleteImagenesByPropiedad(propiedadId: Long)

    @Query("SELECT COUNT(*) FROM imagenes_propiedad WHERE propiedadId = :propiedadId")
    suspend fun getImagenesCount(propiedadId: Long): Int
}
