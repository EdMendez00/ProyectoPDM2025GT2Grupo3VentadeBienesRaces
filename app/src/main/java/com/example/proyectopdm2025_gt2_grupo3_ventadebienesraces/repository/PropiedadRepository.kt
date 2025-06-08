package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.repository

import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * Repositorio para gestionar las operaciones relacionadas con las propiedades inmobiliarias en Firestore
 */
class PropiedadRepository {
    private val db = FirebaseFirestore.getInstance()
    private val propiedadesCollection = db.collection("propiedades")

    /**
     * Obtiene todas las propiedades disponibles ordenadas por fecha de publicación
     */
    fun getAllPropiedades(): Query {
        return propiedadesCollection
            .whereEqualTo("estado", "DISPONIBLE")
            .orderBy("fechaPublicacion", Query.Direction.DESCENDING)
    }

    /**
     * Obtiene todas las propiedades marcadas como vendidas
     */
    fun getPropiedadesVendidas(): Query {
        return propiedadesCollection
            .whereEqualTo("estado", "VENDIDA")
            .orderBy("fechaPublicacion", Query.Direction.DESCENDING)
    }

    /**
     * Obtiene todas las propiedades de un vendedor específico
     */
    fun getPropiedadesByVendedor(vendedorId: String): Query {
        return propiedadesCollection
            .whereEqualTo("vendedorId", vendedorId)
            .orderBy("fechaPublicacion", Query.Direction.DESCENDING)
    }

    /**
     * Obtiene una propiedad específica por su ID
     */
    fun getPropiedad(propiedadId: String): Task<Propiedad> {
        return propiedadesCollection.document(propiedadId).get()
            .continueWith { task ->
                val document = task.result
                if (document != null && document.exists()) {
                    document.toObject(Propiedad::class.java)
                        ?: throw Exception("Error al convertir el documento a Propiedad")
                } else {
                    throw Exception("Propiedad no encontrada")
                }
            }
    }

    /**
     * Crea una nueva propiedad en Firestore
     */
    fun createPropiedad(propiedad: Propiedad): Task<DocumentReference> {
        return propiedadesCollection.add(propiedad)
    }

    /**
     * Actualiza una propiedad existente
     */
    fun updatePropiedad(propiedad: Propiedad): Task<Void> {
        // El ID está excluido de la serialización, por lo que necesitamos obtenerlo separadamente
        val propiedadId = propiedad.id
        if (propiedadId.isEmpty()) {
            return Tasks.forException(IllegalArgumentException("ID de propiedad no válido"))
        }

        return propiedadesCollection.document(propiedadId).set(propiedad)
    }

    /**
     * Marca una propiedad como vendida
     */
    fun marcarComoVendida(propiedadId: String): Task<Void> {
        return propiedadesCollection.document(propiedadId)
            .update("estado", "VENDIDA")
    }

    /**
     * Elimina una propiedad de la base de datos
     */
    fun deletePropiedad(propiedadId: String): Task<Void> {
        return propiedadesCollection.document(propiedadId).delete()
    }

    /**
     * Busca propiedades que coincidan con el texto de búsqueda
     */
    fun searchPropiedades(query: String): Query {
        return propiedadesCollection
            .whereEqualTo("estado", "DISPONIBLE")
            .orderBy("titulo")
            .startAt(query)
            .endAt(query + "\uf8ff")
    }

    /**
     * Obtiene propiedades dentro de un rango de precios
     */
    fun getPropiedadesByPrecio(minPrecio: Double, maxPrecio: Double): Query {
        return propiedadesCollection
            .whereEqualTo("estado", "DISPONIBLE")
            .whereGreaterThanOrEqualTo("precio", minPrecio)
            .whereLessThanOrEqualTo("precio", maxPrecio)
    }
}
