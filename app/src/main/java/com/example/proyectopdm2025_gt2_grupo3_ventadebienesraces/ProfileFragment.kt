package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< Updated upstream
=======
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
>>>>>>> Stashed changes

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
<<<<<<< Updated upstream
=======
            })

            // Establecer la página inicial a "Mis Publicaciones"
            viewPager.currentItem = 0
            updateTabStyles(0)
        }
    }

    private fun setupClickListeners() {
        // Botón de cerrar sesión
        btnLogout.setOnClickListener {
            auth.signOut()
            // Navegación a la pantalla de login
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Botón de agregar propiedad
        btnAddProperty.setOnClickListener {
            // Navegamos al fragmento de agregar propiedad mediante BottomNavigationView
            val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav?.selectedItemId = R.id.addFragment
        }

        // Ícono de home
        layoutHomeIcon.setOnClickListener {
            // Navegar al fragmento de inicio (usando BottomNavigationView)
            val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav?.selectedItemId = R.id.homeFragment
        }
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser

        if (currentUser != null && ::profileEmail.isInitialized && ::profileName.isInitialized) {
            // Mostrar email del usuario
            profileEmail.text = currentUser.email

            // Guardar el email del usuario para usar en la creación de propiedades
            val sharedPrefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            sharedPrefs.edit().putString("user_email", currentUser.email).apply()

            // Cargar nombre del usuario desde Firestore
            currentUser.uid.let { uid ->
                firestore.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val name = document.getString("name") ?: "Usuario"
                            profileName.text = name
                        } else {
                            profileName.text = "Usuario"
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al cargar datos: ${e.message}")
                        profileName.text = "Usuario"
                    }
>>>>>>> Stashed changes
            }
    }
<<<<<<< Updated upstream
}
=======

    // Actualiza el estilo visual de las pestañas según la selección
    private fun updateTabStyles(selectedPosition: Int) {
        if (!::tabMyPublications.isInitialized || !::tabFavorites.isInitialized) {
            return
        }

        if (selectedPosition == 0) {
            // Pestaña "Mis Publicaciones" activa
            tabMyPublications.setCardBackgroundColor(resources.getColor(R.color.white, null))
            tabFavorites.setCardBackgroundColor(resources.getColor(R.color.color_gray_border, null))
        } else {
            // Pestaña "Favoritos" activa
            tabMyPublications.setCardBackgroundColor(resources.getColor(R.color.color_gray_border, null))
            tabFavorites.setCardBackgroundColor(resources.getColor(R.color.white, null))
        }
    }

    // Adaptador para las pestañas
    private inner class ProfileTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> MyPublicationsFragment()
                1 -> FavoriteFragment() // Usando la clase FavoriteFragment existente
                else -> throw IndexOutOfBoundsException("Solo hay 2 pestañas")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Liberar recursos del ViewPager si es necesario
        if (::viewPager.isInitialized) {
            viewPager.adapter = null
        }
    }
}

>>>>>>> Stashed changes
