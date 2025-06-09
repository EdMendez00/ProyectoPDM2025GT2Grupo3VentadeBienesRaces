package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters.PublicacionAdapter
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.FavoritosManager
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad

class FavoriteFragment : Fragment() {

    private lateinit var recyclerFavorites: RecyclerView
    private lateinit var emptyLayout: LinearLayout
    private lateinit var btnExplore: MaterialButton
    private lateinit var favoritosManager: FavoritosManager
    private var propiedadesFavoritas = mutableListOf<Propiedad>()
    private lateinit var adapter: PublicacionAdapter

    private val TAG = "FavoriteFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated iniciado")

        // Inicializar vistas
        recyclerFavorites = view.findViewById(R.id.recycler_favorites)
        emptyLayout = view.findViewById(R.id.layout_empty_favorites)
        btnExplore = view.findViewById(R.id.btn_explore_properties)

        // Inicializar el manager de favoritos
        favoritosManager = FavoritosManager(requireContext())

        setupRecyclerView()
        setupListeners()
        cargarFavoritos()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Actualizando lista de favoritos")
        // Actualizar lista de favoritos cada vez que el fragmento se vuelve visible
        cargarFavoritos()
    }

    private fun setupRecyclerView() {
        Log.d(TAG, "Configurando RecyclerView")
        recyclerFavorites.layoutManager = LinearLayoutManager(context)

        // Crear el adaptador con un listener de favoritos
        adapter = PublicacionAdapter(propiedadesFavoritas) { propiedad, esFavorito ->
            // Callback que se ejecuta cuando se hace clic en el corazón
            Log.d(TAG, "Cambio de estado favorito: ${propiedad.id}, esFavorito=$esFavorito")

            if (!esFavorito) {
                // Si se desmarca como favorito, eliminamos de la lista y actualizamos UI
                val position = propiedadesFavoritas.indexOfFirst { it.id == propiedad.id }
                if (position != -1) {
                    propiedadesFavoritas.removeAt(position)
                    adapter.notifyItemRemoved(position)

                    // Verificar si quedaron favoritos
                    if (propiedadesFavoritas.isEmpty()) {
                        showEmptyState(true)
                    }
                }
            }
        }

        recyclerFavorites.adapter = adapter
    }

    private fun setupListeners() {
        btnExplore.setOnClickListener {
            Log.d(TAG, "Botón Explore pulsado, navegando a Home")
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        try {
            // Intentar navegar a Home
            requireActivity().findViewById<View>(R.id.homeFragment)?.performClick()
        } catch (e: Exception) {
            Log.e(TAG, "Error al navegar a Home: ${e.message}")
            Toast.makeText(context, "No se pudo navegar a la pantalla de inicio", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarFavoritos() {
        try {
            Log.d(TAG, "Cargando favoritos desde almacenamiento")
            // Obtener propiedades favoritas desde el gestor de favoritos
            val nuevasFavoritas = favoritosManager.getPropiedadesFavoritas()

            Log.d(TAG, "Se encontraron ${nuevasFavoritas.size} favoritos")

            // Actualizar la lista y UI
            propiedadesFavoritas.clear()

            if (nuevasFavoritas.isEmpty()) {
                Log.d(TAG, "No hay favoritos, mostrando estado vacío")
                showEmptyState(true)
            } else {
                Log.d(TAG, "Mostrando favoritos: ${nuevasFavoritas.size}")
                showEmptyState(false)
                propiedadesFavoritas.addAll(nuevasFavoritas)
                adapter.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al cargar favoritos: ${e.message}", e)
            showEmptyState(true)
        }
    }

    private fun showEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            recyclerFavorites.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        } else {
            recyclerFavorites.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }
}
