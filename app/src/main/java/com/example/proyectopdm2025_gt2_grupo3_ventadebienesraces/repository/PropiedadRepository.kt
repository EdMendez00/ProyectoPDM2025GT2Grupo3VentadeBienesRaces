package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.repository

import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class PropiedadRepository {
    private val db = FirebaseFirestore.getInstance()
    private val propiedadesCollection = db.collection("propiedades")

    fun getAllPropiedades(): Query {
        return propiedadesCollection.whereEqualTo("estado", "DISPONIBLE")
            .orderBy("fechaPublicacion", Query.Direction.DESCENDING)
    }

    fun getPropiedadesVendidas(): Query {
        return propiedadesCollection.whereEqualTo("estado", "VENDIDA")
            .orderBy("fechaPublicacion", Query.Direction.DESCENDING)
    }

    fun getPropiedadesByVendedor(vendedorId: String): Query {
        return propiedadesCollection
            .whereEqualTo("vendedorId", vendedorId)
            .orderBy("fechaPublicacion", Query.Direction.DESCENDING)
    }

    fun getPropiedad(propiedadId: String): Task<Propiedad> {
        return propiedadesCollection.document(propiedadId).get()
            .continueWith { task ->
                val document = task.result
                if (document.exists()) {
                    document.toObject(Propiedad::class.java)
                } else {
                    throw Exception("Propiedad no encontrada")
                }
            }
    }

    fun createPropiedad(propiedad: Propiedad): Task<DocumentReference> {
        return propiedadesCollection.add(propiedad)
    }

    fun updatePropiedad(propiedad: Propiedad): Task<Void> {
        return propiedadesCollection.document(propiedad.id).set(propiedad)
    }

    fun marcarComoVendida(propiedadId: String): Task<Void> {
        return propiedadesCollection.document(propiedadId)
            .update("estado", "VENDIDA")
    }

    fun deletePropiedad(propiedadId: String): Task<Void> {
        return propiedadesCollection.document(propiedadId).delete()
    }

    fun searchPropiedades(query: String): Query {
        return propiedadesCollection
            .whereEqualTo("estado", "DISPONIBLE")
            .orderBy("titulo")
            .startAt(query)
            .endAt(query + "\uf8ff")
    }

    fun getPropiedadesByPrecio(minPrecio: Double, maxPrecio: Double): Query {
        return propiedadesCollection
            .whereEqualTo("estado", "DISPONIBLE")
            .whereGreaterThanOrEqualTo("precio", minPrecio)
            .whereLessThanOrEqualTo("precio", maxPrecio)
    }
}
