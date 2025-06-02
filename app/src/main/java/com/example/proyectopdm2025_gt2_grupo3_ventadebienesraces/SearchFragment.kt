package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class SearchFragment : Fragment() {

    private val categories = arrayOf("Casa", "Apartamento", "Terreno", "Comercial")
    private val filter = arrayOf("Precio Alto", "Precio Bajo", "Más Reciente", "Más Antiguo")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val categorySpinner = view.findViewById<AutoCompleteTextView>(R.id.spinner_category)
        val filterSpinner = view.findViewById<AutoCompleteTextView>(R.id.spinner_filter)

        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        val filterAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, filter)

        categorySpinner.setAdapter(categoryAdapter)
        filterSpinner.setAdapter(filterAdapter)

        return view
    }

}