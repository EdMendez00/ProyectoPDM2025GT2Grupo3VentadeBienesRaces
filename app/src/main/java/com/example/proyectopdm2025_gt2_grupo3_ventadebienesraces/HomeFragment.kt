package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters.PublicacionAdapter
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Dimensiones
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Ubicacion
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.viewmodel.PropiedadViewModel
import android.widget.TextView
import android.util.Log
import java.util.Date

class HomeFragment : Fragment() {

    private lateinit var propiedadViewModel: PropiedadViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoProperties: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar ViewModel
        propiedadViewModel = ViewModelProvider(this).get(PropiedadViewModel::class.java)

        // Configurar RecyclerView
        recyclerView = view.findViewById<RecyclerView>(R.id.rvPublicaciones)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Mensaje cuando no hay propiedades
        tvNoProperties = view.findViewById(R.id.tvNoProperties)

        // Inicialmente, mostrar mensaje de carga
        mostrarMensajeCarga()

        // Observar propiedades de la base de datos - ESTA ES LA PARTE PRINCIPAL
        observarPropiedadesBaseDeDatos()
    }

    private fun mostrarMensajeCarga() {
        tvNoProperties.text = "Cargando propiedades..."
        tvNoProperties.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun observarPropiedadesBaseDeDatos() {
        // Observar cambios en las propiedades disponibles desde la base de datos
        propiedadViewModel.propiedadesDisponibles.observe(viewLifecycleOwner) { propiedadesEntity ->
            Log.d("HomeFragment", "Propiedades obtenidas desde DB: ${propiedadesEntity.size}")

            if (propiedadesEntity.isNotEmpty()) {
                // Convertir PropiedadEntity a Propiedad (modelo para la UI)
                val propiedades = propiedadesEntity.map { entity ->
                    Propiedad(
                        id = entity.id.toString(), // Convertir Long a String
                        titulo = entity.titulo,
                        descripcion = entity.descripcion,
                        precio = entity.precio,
                        ubicacion = Ubicacion(
                            direccion = entity.direccion,
                            latitud = entity.latitud,
                            longitud = entity.longitud
                        ),
                        dimensiones = Dimensiones(
                            largo = entity.largo,
                            ancho = entity.ancho,
                            area = entity.area
                        ),
                        // Convertir string separado por comas a lista
                        caracteristicas = entity.caracteristicas
                            .split(",")
                            .filter { it.isNotEmpty() },
                        // Por ahora, no tenemos imágenes
                        imagenes = emptyList(),
                        estado = entity.estado,
                        medioContacto = entity.medioContacto,
                        vendedorId = entity.vendedorId,
                        fechaPublicacion = entity.fechaPublicacion
                    )
                }

                // Actualizar el adapter con las propiedades convertidas
                val adapter = PublicacionAdapter(propiedades)
                recyclerView.adapter = adapter

                // Mostrar RecyclerView y ocultar mensaje
                tvNoProperties.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE

                Log.d("HomeFragment", "Propiedades mostradas: ${propiedades.size}")
            } else {
                // Si no hay propiedades, mostrar datos de ejemplo
                cargarPropiedadesPrueba()

                Log.d("HomeFragment", "No hay propiedades en la DB, mostrando datos de ejemplo")
            }
        }
    }

    private fun cargarPropiedadesPrueba() {
        // Datos de prueba para mostrar mientras se implementa la base de datos
        val propiedades = listOf(
            Propiedad(
                id = "1",
                titulo = "Casa Moderna en Ciudad",
                descripcion = "Casa moderna con acabados de lujo y excelente ubicación.",
                precio = 150000.0,
                ubicacion = Ubicacion(
                    direccion = "Colonia Escalón, Calle 5, San Salvador",
                    latitud = 13.6989,
                    longitud = -89.1914
                ),
                dimensiones = Dimensiones(
                    largo = 25.0,
                    ancho = 10.0,
                    area = 250.0
                ),
                caracteristicas = listOf("3 dormitorios", "2 baños", "Cocina integral", "Jardín"),
                imagenes = listOf("https://images.unsplash.com/photo-1678895223308-da40c609e39b"),
                estado = "DISPONIBLE",
                medioContacto = "Tel: 2222-3333",
                vendedorId = "user123",
                fechaPublicacion = Date()
            ),
            Propiedad(
                id = "2",
                titulo = "Apartamento de Lujo",
                descripcion = "Apartamento con vista panorámica y amenidades exclusivas.",
                precio = 120000.0,
                ubicacion = Ubicacion(
                    direccion = "Av. Las Palmas #12, Santa Tecla",
                    latitud = 13.6731,
                    longitud = -89.2797
                ),
                dimensiones = Dimensiones(
                    largo = 12.0,
                    ancho = 10.0,
                    area = 120.0
                ),
                caracteristicas = listOf("2 dormitorios", "2 baños", "Piscina", "Gimnasio"),
                imagenes = listOf("https://images.unsplash.com/photo-1748050868829-74a5a43f79c6"),
                estado = "DISPONIBLE",
                medioContacto = "Tel: 5555-6666",
                vendedorId = "user456",
                fechaPublicacion = Date()
            )
        )

        // Actualizar el RecyclerView con los datos de prueba
        val adapter = PublicacionAdapter(propiedades)
        recyclerView.adapter = adapter

        // Mostrar RecyclerView y ocultar mensaje
        tvNoProperties.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}