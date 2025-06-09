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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters.PublicacionAdapter
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
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

                // Configurar ViewPager
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
    }

    private fun setupViewPager() {
        // Configurar el adaptador para ViewPager2 si la vista ya está inicializada
        if (::viewPager.isInitialized) {
            viewPager.adapter = ProfileTabAdapter(this)
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
            activity?.finish()
        }

        // Botón de agregar propiedad
        btnAddProperty.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, AddFragment())
                .addToBackStack(null)
                .commit()
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

    // Adaptador para el ViewPager - ahora solo muestra favoritos
    private inner class ProfileTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = 1  // Ahora solo tenemos una página: Favoritos

        override fun createFragment(position: Int): Fragment {
            return FavoriteFragment()  // Siempre devolvemos FavoriteFragment
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
