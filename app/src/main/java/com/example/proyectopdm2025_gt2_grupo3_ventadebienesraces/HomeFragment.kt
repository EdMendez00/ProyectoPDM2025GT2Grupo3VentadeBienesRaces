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
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

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
        propiedadViewModel.propiedadesDisponibles.observe(viewLifecycleOwner, Observer { propiedadesEntity ->
            Log.d("HomeFragment", "Propiedades obtenidas desde DB: ${propiedadesEntity.size}")

            if (propiedadesEntity.isNotEmpty()) {
                // Tenemos propiedades en la base de datos
                val propiedadesConvertidas = mutableListOf<Propiedad>()
                val contador = AtomicInteger(propiedadesEntity.size)

                // Procesar cada propiedad
                for (entity in propiedadesEntity) {
                    // Primero, convertir la propiedad básica
                    val propiedadConvertida = Propiedad(
                        id = entity.id.toString(),
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
                        caracteristicas = entity.caracteristicas.split(",").filter { it.isNotEmpty() },
                        imagenes = emptyList(), // Inicialmente vacío, lo llenaremos después
                        estado = entity.estado,
                        medioContacto = entity.medioContacto,
                        vendedorId = entity.vendedorId,
                        fechaPublicacion = entity.fechaPublicacion
                    )

                    // Ahora, cargar las imágenes asociadas a esta propiedad
                    propiedadViewModel.getImagenesByPropiedad(entity.id).observe(viewLifecycleOwner, Observer { imagenesEntity ->
                        // Extraer rutas de imágenes
                        val rutasImagenes = imagenesEntity.map { it.rutaImagen }

                        // Clonar la propiedad con la lista de imágenes actualizada
                        val propiedadFinal = propiedadConvertida.copy(imagenes = rutasImagenes)

                        // Añadir a la lista
                        propiedadesConvertidas.add(propiedadFinal)

                        Log.d("HomeFragment", "Propiedad ${entity.id} tiene ${rutasImagenes.size} imágenes")

                        // Si es la última propiedad, actualizar el UI
                        if (contador.decrementAndGet() == 0) {
                            mostrarPropiedades(propiedadesConvertidas)
                        }
                    })
                }

                // En caso de que no haya imágenes (para evitar bloqueos)
                CoroutineScope(Dispatchers.Main).launch {
                    // Esperar un poco y verificar si tenemos propiedades
                    withContext(Dispatchers.IO) {
                        Thread.sleep(500)
                    }

                    if (propiedadesConvertidas.isNotEmpty()) {
                        mostrarPropiedades(propiedadesConvertidas)
                    } else if (contador.get() > 0) {
                        // Aún estamos esperando las imágenes, mostrar las propiedades sin imágenes
                        for (entity in propiedadesEntity) {
                            val propiedadSinImagenes = Propiedad(
                                id = entity.id.toString(),
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
                                caracteristicas = entity.caracteristicas.split(",").filter { it.isNotEmpty() },
                                imagenes = emptyList(),
                                estado = entity.estado,
                                medioContacto = entity.medioContacto,
                                vendedorId = entity.vendedorId,
                                fechaPublicacion = entity.fechaPublicacion
                            )
                            propiedadesConvertidas.add(propiedadSinImagenes)
                        }
                        mostrarPropiedades(propiedadesConvertidas)
                    }
                }
            } else {
                // Si no hay propiedades, mostrar datos de ejemplo
                cargarPropiedadesPrueba()
                Log.d("HomeFragment", "No hay propiedades en la DB, mostrando datos de ejemplo")
            }
        })
    }

    private fun mostrarPropiedades(propiedades: List<Propiedad>) {
        if (view == null) return  // Evitar crash si el fragmento ya no está adjunto

        // Actualizar el adapter con las propiedades
        val adapter = PublicacionAdapter(propiedades)
        recyclerView.adapter = adapter

        // Mostrar RecyclerView y ocultar mensaje
        tvNoProperties.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        Log.d("HomeFragment", "Propiedades mostradas: ${propiedades.size}")
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

        mostrarPropiedades(propiedades)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}