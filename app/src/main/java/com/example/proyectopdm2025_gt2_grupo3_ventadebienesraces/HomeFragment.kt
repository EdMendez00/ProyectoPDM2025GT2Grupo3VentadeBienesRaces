package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters.PublicacionAdapter
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Dimensiones
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Ubicacion

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvPublicaciones)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val publicaciones = listOf(
            Propiedad(
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
                imagenes = listOf("https://images.unsplash.com/photo-1678895223308-da40c609e39b?q=80&w=1888&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                estado = "DISPONIBLE",
                medioContacto = "Tel: 2222-3333"
            ),
            Propiedad(
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
                imagenes = listOf("https://images.unsplash.com/photo-1748050868829-74a5a43f79c6?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                estado = "DISPONIBLE",
                medioContacto = "Tel: 5555-6666"
            ),
            Propiedad(
                titulo = "Residencia Familiar",
                descripcion = "Amplia residencia ideal para familias grandes.",
                precio = 200000.0,
                ubicacion = Ubicacion(
                    direccion = "Calle El Mirador, Km 10, La Libertad",
                    latitud = 13.4883,
                    longitud = -89.3226
                ),
                dimensiones = Dimensiones(
                    largo = 35.0,
                    ancho = 10.0,
                    area = 350.0
                ),
                caracteristicas = listOf("4 dormitorios", "3 baños", "Terraza", "Cochera"),
                imagenes = listOf("https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd"),
                estado = "DISPONIBLE",
                medioContacto = "Tel: 7777-8888"
            )
        )
        recyclerView.adapter = PublicacionAdapter(publicaciones)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}