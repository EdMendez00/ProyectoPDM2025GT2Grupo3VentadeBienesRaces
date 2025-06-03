package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.repository

import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Usuario
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class UsuarioRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usuariosCollection = db.collection("usuarios")
    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getUsuario(userId: String): Task<Usuario> {
        return usuariosCollection.document(userId).get()
            .continueWith { task ->
                val document = task.result
                if (document.exists()) {
                    document.toObject(Usuario::class.java)
                } else {
                    throw Exception("Usuario no encontrado")
                }
            }
    }

    fun createUsuario(usuario: Usuario): Task<Void> {
        return usuariosCollection.document(usuario.id).set(usuario)
    }

    fun updateUsuario(usuario: Usuario): Task<Void> {
        return usuariosCollection.document(usuario.id).set(usuario)
    }

    fun deleteUsuario(userId: String): Task<Void> {
        return usuariosCollection.document(userId).delete()
    }

    fun getAllUsuarios(): Task<QuerySnapshot> {
        return usuariosCollection.get()
    }

    fun getUsuariosByRol(rol: String): Query {
        return usuariosCollection.whereEqualTo("rol", rol)
    }
}
