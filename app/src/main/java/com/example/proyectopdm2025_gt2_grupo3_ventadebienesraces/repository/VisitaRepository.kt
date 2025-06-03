package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.repository

import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Visita
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class VisitaRepository {
    private val db = FirebaseFirestore.getInstance()
    private val visitasCollection = db.collection("visitas")

    fun createVisita(visita: Visita): Task<DocumentReference> {
        return visitasCollection.add(visita)
    }

    fun getVisitasByPropiedad(propiedadId: String): Query {
        return visitasCollection
            .whereEqualTo("propiedadId", propiedadId)
            .orderBy("fecha", Query.Direction.DESCENDING)
    }

    fun getVisitasByCliente(clienteId: String): Query {
        return visitasCollection
            .whereEqualTo("clienteId", clienteId)
            .orderBy("fecha", Query.Direction.DESCENDING)
    }

    fun getVisitasByVendedor(vendedorId: String): Query {
        return visitasCollection
            .whereEqualTo("vendedorId", vendedorId)
            .orderBy("fecha", Query.Direction.DESCENDING)
    }

    fun getVisitasPendientes(vendedorId: String): Query {
        return visitasCollection
            .whereEqualTo("vendedorId", vendedorId)
            .whereEqualTo("estado", "PENDIENTE")
            .orderBy("fecha", Query.Direction.ASCENDING)
    }

    fun updateEstadoVisita(visitaId: String, nuevoEstado: String): Task<Void> {
        return visitasCollection.document(visitaId)
            .update("estado", nuevoEstado)
    }

    fun getVisitasFuturas(propiedadId: String): Query {
        val ahora = Timestamp.now()
        return visitasCollection
            .whereEqualTo("propiedadId", propiedadId)
            .whereGreaterThan("fecha", ahora)
            .orderBy("fecha", Query.Direction.ASCENDING)
    }

    fun deleteVisita(visitaId: String): Task<Void> {
        return visitasCollection.document(visitaId).delete()
    }

    fun updateComentariosVisita(visitaId: String, comentarios: String): Task<Void> {
        return visitasCollection.document(visitaId)
            .update("comentarios", comentarios)
    }

    fun getHistorialCompleto(propiedadId: String): Query {
        return visitasCollection
            .whereEqualTo("propiedadId", propiedadId)
            .whereEqualTo("estado", "REALIZADA")
            .orderBy("fecha", Query.Direction.DESCENDING)
    }
}
