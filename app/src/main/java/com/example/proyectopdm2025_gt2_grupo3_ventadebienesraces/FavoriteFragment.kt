package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class FavoriteFragment : Fragment() {

    private lateinit var recyclerFavorites: RecyclerView
    private lateinit var emptyLayout: LinearLayout
    private lateinit var btnExplore: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar vistas
        recyclerFavorites = view.findViewById(R.id.recycler_favorites)
        emptyLayout = view.findViewById(R.id.layout_empty_favorites)
        btnExplore = view.findViewById(R.id.btn_explore_properties)

        setupRecyclerView()
        setupListeners()

        // Por ahora mostraremos el estado vacío hasta implementar el backend
        showEmptyState(true)
    }

    private fun setupRecyclerView() {
        recyclerFavorites.layoutManager = LinearLayoutManager(context)
        // Aquí se configurará el adaptador cuando implementemos el backend
        // recyclerFavorites.adapter = favoritesAdapter
    }

    private fun setupListeners() {
        btnExplore.setOnClickListener {
            // Navegar a la sección de explorar propiedades
            // Por implementar cuando se integre con el backend
            requireActivity().findViewById<View>(R.id.homeFragment).performClick()
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

