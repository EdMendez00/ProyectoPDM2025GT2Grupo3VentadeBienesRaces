package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class AgendarVisitaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agendar_visita, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val tvTitleAdd = view.findViewById<TextView>(R.id.tvTitleAdd)
        val tvDirection = view.findViewById<TextView>(R.id.tvDirection)
        val tvInfo = view.findViewById<TextView>(R.id.tvInfo)
        val nombreDepartamento = args?.getString("NOMBRE_DEPARTAMENTO") ?: ""
        val direccion = args?.getString("DIRECCION") ?: ""
        val nombreVendedor = args?.getString("NOMBRE_VENDEDOR") ?: ""
        tvTitleAdd.text = nombreDepartamento
        tvDirection.text = direccion
        tvInfo.text = "Programe una cita con $nombreVendedor"

        // Horas disponibles de prueba para el vendedor
        val horasDisponibles = listOf(
            "09:00 AM", "10:00 AM", "11:00 AM", "02:00 PM", "03:00 PM", "04:00 PM"
        )
        val actvHoraComprador = view.findViewById<AutoCompleteTextView>(R.id.actvHoraComprador)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, horasDisponibles)
        actvHoraComprador.setAdapter(adapter)
    }
}