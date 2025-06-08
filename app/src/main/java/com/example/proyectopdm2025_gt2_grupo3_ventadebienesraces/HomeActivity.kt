package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val addFragment = AddFragment()
    private val favoriteFragment = FavoriteFragment()
    private val profileFragment = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> loadFragment(homeFragment)
                R.id.searchFragment -> loadFragment(searchFragment)
                R.id.addFragment -> loadFragment(addFragment)
                R.id.favoriteFragment -> loadFragment(favoriteFragment)
                R.id.profileFragment -> loadFragment(profileFragment)
            }
            true
        }

<<<<<<< Updated upstream
        // Load the default fragment
        loadFragment(homeFragment)
=======
        // Verificar si se debe navegar a una sección específica
        val navigateTo = intent.getStringExtra("NAVIGATE_TO")
        if (navigateTo == "MIS_PUBLICACIONES") {
            Log.d("HomeActivity", "Navegando a Mis Publicaciones")
            navigation.selectedItemId = R.id.profileFragment
        } else {
            navigation.selectedItemId = R.id.homeFragment
        }
>>>>>>> Stashed changes
    }

    private fun loadFragment(fragment: Fragment) {
        // Replace the current fragment with the selected one
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }
}