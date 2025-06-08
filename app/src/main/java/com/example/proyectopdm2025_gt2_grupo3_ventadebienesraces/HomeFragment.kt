package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters.PropertyAdapter
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.PropiedadEntity
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.viewmodel.PropiedadViewModel

class HomeFragment : Fragment() {
    private lateinit var propiedadViewModel: PropiedadViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewNoProps: TextView
    private lateinit var editTextSearch: EditText

    // Pestañas de filtrado
    private lateinit var tabAll: CardView
    private lateinit var tabHouses: CardView
    private lateinit var tabApartments: CardView

    // Lista de propiedades
    private var allProperties = listOf<PropiedadEntity>()

    // Tipo seleccionado actualmente
    private var currentFilter = "TODAS"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Mantener el layout original del fragment home
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inicializar vistas
        initViews(view)

        // Configurar listeners para las pestañas
        setupTabListeners()

        // Cargar propiedades
        loadProperties()

        return view
    }

    private fun initViews(view: View) {
        // Inicializar el RecyclerView para propiedades
        recyclerView = view.findViewById(R.id.recycler_properties)
        textViewNoProps = view.findViewById(R.id.tv_no_properties)
        editTextSearch = view.findViewById(R.id.et_search)

        // Inicializar pestañas
        tabAll = view.findViewById(R.id.tab_all_properties)
        tabHouses = view.findViewById(R.id.tab_houses)
        tabApartments = view.findViewById(R.id.tab_apartments)

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupTabListeners() {
        // Configurar los listeners para las pestañas
        tabAll.setOnClickListener {
            updateTabStyles(0)
            filterProperties("TODAS")
        }

        tabHouses.setOnClickListener {
            updateTabStyles(1)
            filterProperties("Casa")
        }

        tabApartments.setOnClickListener {
            updateTabStyles(2)
            filterProperties("Apartamento")
        }
    }

    private fun updateTabStyles(selectedTab: Int) {
        // Restablecer todos los fondos
        tabAll.setCardBackgroundColor(resources.getColor(R.color.color_gray_border, null))
        tabHouses.setCardBackgroundColor(resources.getColor(R.color.color_gray_border, null))
        tabApartments.setCardBackgroundColor(resources.getColor(R.color.color_gray_border, null))

        // Restablecer colores de texto para texto gris
        (tabAll.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.gray_text, null))
        (tabHouses.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.gray_text, null))
        (tabApartments.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.gray_text, null))

        // Configurar el fondo de la pestaña seleccionada y color de texto azul
        when (selectedTab) {
            0 -> {
                tabAll.setCardBackgroundColor(resources.getColor(R.color.white, null))
                (tabAll.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.blue_normal, null))
            }
            1 -> {
                tabHouses.setCardBackgroundColor(resources.getColor(R.color.white, null))
                (tabHouses.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.blue_normal, null))
            }
            2 -> {
                tabApartments.setCardBackgroundColor(resources.getColor(R.color.white, null))
                (tabApartments.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.blue_normal, null))
            }
        }
    }

    private fun loadProperties() {
        propiedadViewModel = ViewModelProvider(this).get(PropiedadViewModel::class.java)

        // Observar las propiedades disponibles
        propiedadViewModel.getPropiedadesDisponibles().observe(viewLifecycleOwner) { propiedades ->
            // Guardar todas las propiedades
            allProperties = propiedades

            // Aplicar el filtro actual
            filterProperties(currentFilter)
        }
    }

    private fun filterProperties(filter: String) {
        currentFilter = filter

        val filteredList = when(filter) {
            "TODAS" -> allProperties
            else -> allProperties.filter { it.titulo.contains(filter, ignoreCase = true) }
        }

        if (filteredList.isNotEmpty()) {
            Log.d("HomeFragment", "Propiedades filtradas: ${filteredList.size}")

            // Mostrar las propiedades en el RecyclerView
            recyclerView.adapter = PropertyAdapter(filteredList) { propiedad ->
                // Aquí manejaremos el clic en una propiedad para ver detalles
                Toast.makeText(context, "Propiedad: ${propiedad.titulo}", Toast.LENGTH_SHORT).show()
                // TODO: Implementar navegación a la pantalla de detalle de la propiedad
            }

            recyclerView.visibility = View.VISIBLE
            textViewNoProps.visibility = View.GONE
        } else {
            Log.d("HomeFragment", "No se encontraron propiedades para el filtro: $filter")
            textViewNoProps.text = "No hay propiedades disponibles"
            recyclerView.visibility = View.GONE
            textViewNoProps.visibility = View.VISIBLE
        }
    }
}
