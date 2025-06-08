package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters.PropertyAdapter
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.viewmodel.PropiedadViewModel

class MyPublicationsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var propiedadViewModel: PropiedadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_property_list, container, false)

        recyclerView = view.findViewById(R.id.recycler_properties)
        emptyView = view.findViewById(R.id.empty_view)

        setupRecyclerView()
        loadProperties()

        return view
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadProperties() {
        propiedadViewModel = ViewModelProvider(this).get(PropiedadViewModel::class.java)

        // Obtener el ID del usuario actual
        val sharedPrefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userEmail = sharedPrefs.getString("user_email", "") ?: ""
        val userId = if (userEmail.isNotEmpty()) {
            "user_" + userEmail.replace("@", "_at_").replace(".", "_dot_")
        } else {
            // Si no hay email guardado, mostramos mensaje de que no hay propiedades
            Log.d("MyPublicationsFragment", "No hay email de usuario guardado")
            emptyView.text = "Inicia sesión para ver tus propiedades"
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            return
        }

        Log.d("MyPublicationsFragment", "Buscando propiedades para vendedor: $userId")

        // Observar las propiedades del vendedor
        propiedadViewModel.getPropiedadesByVendedor(userId).observe(viewLifecycleOwner) { propiedades ->
            if (propiedades.isNotEmpty()) {
                Log.d("MyPublicationsFragment", "Propiedades encontradas: ${propiedades.size}")
                recyclerView.adapter = PropertyAdapter(propiedades) { propiedad ->
                    // Aquí maneja el clic en una propiedad (para ver detalles)
                    Toast.makeText(context, "Propiedad: ${propiedad.titulo}", Toast.LENGTH_SHORT).show()
                }
                recyclerView.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
            } else {
                Log.d("MyPublicationsFragment", "No se encontraron propiedades")
                emptyView.text = "No tienes propiedades publicadas"
                recyclerView.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
            }
        }
    }
}
