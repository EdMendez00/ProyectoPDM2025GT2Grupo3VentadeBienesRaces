package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.AppDatabase
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao.ImagenPropiedadDao
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.dao.PropiedadDao
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.ImagenPropiedadEntity
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.PropiedadEntity
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Ubicacion
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Dimensiones
import kotlinx.coroutines.launch

class PropiedadViewModel(application: Application) : AndroidViewModel(application) {

    private val propiedadDao: PropiedadDao
    private val imagenPropiedadDao: ImagenPropiedadDao

    // Exponemos directamente las propiedades disponibles como LiveData
    val propiedadesDisponibles: LiveData<List<PropiedadEntity>>
    val propiedadesVendidas: LiveData<List<PropiedadEntity>>

    init {
        val database = AppDatabase.getDatabase(application)
        propiedadDao = database.propiedadDao()
        imagenPropiedadDao = database.imagenPropiedadDao()

        // Inicializamos las propiedades LiveData
        propiedadesDisponibles = propiedadDao.getPropiedadesDisponibles()
        propiedadesVendidas = propiedadDao.getPropiedadesVendidas()
    }

    // Propiedades de un vendedor específico
    fun getPropiedadesByVendedor(vendedorId: String): LiveData<List<PropiedadEntity>> {
        return propiedadDao.getPropiedadesByVendedor(vendedorId)
    }

    // Detalles de una propiedad
    fun getPropiedadById(propiedadId: Long): LiveData<PropiedadEntity> {
        return propiedadDao.getPropiedadById(propiedadId)
    }

    // Insertar nueva propiedad
    fun insertPropiedad(propiedad: PropiedadEntity, onComplete: (propiedadId: Long) -> Unit) {
        viewModelScope.launch {
            val propiedadId = propiedadDao.insertPropiedad(propiedad)
            onComplete(propiedadId)
        }
    }

    // Actualizar propiedad existente
    fun updatePropiedad(propiedad: PropiedadEntity) {
        viewModelScope.launch {
            propiedadDao.updatePropiedad(propiedad)
        }
    }

    // Marcar propiedad como vendida
    fun marcarComoVendida(propiedadId: Long) {
        viewModelScope.launch {
            propiedadDao.marcarComoVendida(propiedadId)
        }
    }

    // Eliminar propiedad
    fun deletePropiedad(propiedad: PropiedadEntity) {
        viewModelScope.launch {
            propiedadDao.deletePropiedad(propiedad)
        }
    }

    // Buscar propiedades por texto
    fun searchPropiedades(query: String): LiveData<List<PropiedadEntity>> {
        return propiedadDao.searchPropiedades(query)
    }

    // Filtrar propiedades por rango de precio
    fun getPropiedadesByPrecio(minPrecio: Double, maxPrecio: Double): LiveData<List<PropiedadEntity>> {
        return propiedadDao.getPropiedadesByPrecio(minPrecio, maxPrecio)
    }

    // Obtener imágenes de una propiedad
    fun getImagenesByPropiedad(propiedadId: Long): LiveData<List<ImagenPropiedadEntity>> {
        return imagenPropiedadDao.getImagenesByPropiedad(propiedadId)
    }

    // Insertar imagen
    fun insertImagen(imagen: ImagenPropiedadEntity) {
        viewModelScope.launch {
            imagenPropiedadDao.insertImagen(imagen)
        }
    }

    // Eliminar imagen
    fun deleteImagen(imagen: ImagenPropiedadEntity) {
        viewModelScope.launch {
            imagenPropiedadDao.deleteImagen(imagen)
        }
    }

    // Eliminar todas las imágenes de una propiedad
    fun deleteImagenesByPropiedad(propiedadId: Long) {
        viewModelScope.launch {
            imagenPropiedadDao.deleteImagenesByPropiedad(propiedadId)
        }
    }

    // Contar número de imágenes de una propiedad
    suspend fun getImagenesCount(propiedadId: Long): Int {
        return imagenPropiedadDao.getImagenesCount(propiedadId)
    }
}

// --- FUNCIÓN DE EXTENSIÓN GLOBAL ---

fun com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.PropiedadEntity.toPropiedad(): com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad {
    return com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad(
        id = this.id.toString(),
        titulo = this.titulo,
        descripcion = this.descripcion,
        precio = this.precio,
        ubicacion = com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Ubicacion(
            direccion = this.direccion,
            latitud = this.latitud,
            longitud = this.longitud
        ),
        dimensiones = com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Dimensiones(
            largo = this.largo,
            ancho = this.ancho,
            area = this.area
        ),
        caracteristicas = if (this.caracteristicas.isNotEmpty()) this.caracteristicas.split(",") else listOf(),
        imagenes = listOf(),
        estado = this.estado,
        medioContacto = this.medioContacto,
        vendedorId = this.vendedorId,
        fechaPublicacion = this.fechaPublicacion,
        dormitorios = this.dormitorios,
        banos = this.banos,
        tipoPropiedad = this.tipoPropiedad
    )
}
