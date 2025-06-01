package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "ProfileFragment"

class ProfileFragment : Fragment() {

    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var btnLogout: MaterialButton
    private lateinit var viewPager: ViewPager2
    private lateinit var btnAddProperty: MaterialButton
    private lateinit var layoutHomeIcon: LinearLayout
    private lateinit var layoutMyPublications: LinearLayout

    // Referencias a las pestañas personalizadas
    private lateinit var tabMyPublications: CardView
    private lateinit var tabFavorites: CardView

    // Inicializar Firebase de manera lazy para que se cargue solo cuando se necesite
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    private var isViewInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        try {
            if (!isViewInitialized) {
                // Inicializar vistas
                initViews(view)

                // Configurar ViewPager y pestañas
                setupViewPager()

                // Configurar listeners de clics
                setupClickListeners()

                isViewInitialized = true
            }

            // Cargar datos del usuario - esto siempre se debe hacer para mostrar info actualizada
            loadUserData()

        } catch (e: Exception) {
            Log.e(TAG, "Error al inicializar: ${e.message}")
            Toast.makeText(context, "Error al cargar el perfil", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun initViews(view: View) {
        profileImage = view.findViewById(R.id.profile_image)
        profileName = view.findViewById(R.id.tv_profile_name)
        profileEmail = view.findViewById(R.id.tv_profile_email)
        btnLogout = view.findViewById(R.id.btn_logout)
        viewPager = view.findViewById(R.id.viewpager_profile)
        btnAddProperty = view.findViewById(R.id.btn_add_property)
        layoutHomeIcon = view.findViewById(R.id.layout_home_icon)
        layoutMyPublications = view.findViewById(R.id.layout_my_publications)

        // Inicializar pestañas
        tabMyPublications = view.findViewById(R.id.tab_my_publications)
        tabFavorites = view.findViewById(R.id.tab_favorites)

        // Configurar listeners de las pestañas
        tabMyPublications.setOnClickListener {
            viewPager.currentItem = 0
            updateTabStyles(0)
        }

        tabFavorites.setOnClickListener {
            viewPager.currentItem = 1
            updateTabStyles(1)
        }
    }

    private fun setupViewPager() {
        // Configurar el adaptador para ViewPager2 si la vista ya está inicializada
        if (::viewPager.isInitialized) {
            viewPager.adapter = ProfileTabAdapter(this)

            // Listener para cambios de página
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateTabStyles(position)
                }
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
            // Aquí implementarías la navegación a la pantalla de agregar propiedad
            // Por ejemplo:
            // val intent = Intent(activity, AddPropertyActivity::class.java)
            // startActivity(intent)

            Toast.makeText(context, "Agregar propiedad", Toast.LENGTH_SHORT).show()
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
            }
        } else if (context != null) {
            // Si no hay usuario logueado, redirigir al login
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    // Actualiza el estilo visual de las pestañas según la selección
    private fun updateTabStyles(selectedPosition: Int) {
        if (!::tabMyPublications.isInitialized || !::tabFavorites.isInitialized ||
            !::layoutMyPublications.isInitialized) {
            return
        }

        if (selectedPosition == 0) {
            // Pestaña "Mis Publicaciones" activa
            tabMyPublications.setCardBackgroundColor(resources.getColor(R.color.white, null))
            tabFavorites.setCardBackgroundColor(resources.getColor(R.color.color_gray_border, null))

            // Mostrar las publicaciones del usuario
            layoutMyPublications.visibility = View.VISIBLE
        } else {
            // Pestaña "Favoritos" activa
            tabMyPublications.setCardBackgroundColor(resources.getColor(R.color.color_gray_border, null))
            tabFavorites.setCardBackgroundColor(resources.getColor(R.color.white, null))

            // Ocultar las publicaciones al mostrar favoritos
            layoutMyPublications.visibility = View.GONE
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

// Fragmento para "Mis Publicaciones"
class MyPublicationsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Este fragmento puede estar vacío ya que mostramos el contenido directamente en ProfileFragment
        return View(context)
    }
}

