package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.VisitaEntity
import java.util.Date

@Dao
interface VisitaDao {
    @Query("SELECT * FROM visitas WHERE propiedadId = :propiedadId ORDER BY fecha DESC")
    fun getVisitasByPropiedad(propiedadId: Long): LiveData<List<VisitaEntity>>

    @Query("SELECT * FROM visitas WHERE clienteId = :clienteId ORDER BY fecha DESC")
    fun getVisitasByCliente(clienteId: String): LiveData<List<VisitaEntity>>

    @Query("SELECT * FROM visitas WHERE vendedorId = :vendedorId ORDER BY fecha DESC")
    fun getVisitasByVendedor(vendedorId: String): LiveData<List<VisitaEntity>>

    @Query("SELECT * FROM visitas WHERE vendedorId = :vendedorId AND estado = 'PENDIENTE' ORDER BY fecha ASC")
    fun getVisitasPendientesByVendedor(vendedorId: String): LiveData<List<VisitaEntity>>

    @Query("SELECT * FROM visitas WHERE propiedadId = :propiedadId AND fecha >= :fechaActual ORDER BY fecha ASC")
    fun getVisitasFuturas(propiedadId: Long, fechaActual: Date): LiveData<List<VisitaEntity>>

    @Query("UPDATE visitas SET estado = :nuevoEstado WHERE id = :visitaId")
    suspend fun updateEstadoVisita(visitaId: Long, nuevoEstado: String)

    @Query("UPDATE visitas SET comentarios = :comentarios WHERE id = :visitaId")
    suspend fun updateComentariosVisita(visitaId: Long, comentarios: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisita(visita: VisitaEntity): Long

    @Update
    suspend fun updateVisita(visita: VisitaEntity)

    @Delete
    suspend fun deleteVisita(visita: VisitaEntity)

    @Query("SELECT * FROM visitas WHERE propiedadId = :propiedadId AND estado = 'REALIZADA' ORDER BY fecha DESC")
    fun getHistorialCompleto(propiedadId: Long): LiveData<List<VisitaEntity>>

    @Query("SELECT * FROM visitas WHERE id = :visitaId")
    fun getVisitaById(visitaId: Long): LiveData<VisitaEntity>
}
