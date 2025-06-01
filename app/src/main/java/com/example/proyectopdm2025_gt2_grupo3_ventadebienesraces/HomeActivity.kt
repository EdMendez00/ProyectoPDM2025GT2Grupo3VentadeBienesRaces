package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private val homeFragment by lazy { HomeFragment() }
    private val searchFragment by lazy { SearchFragment() }
    private val addFragment by lazy { AddFragment() }
    private val favoriteFragment by lazy { FavoriteFragment() }
    private val profileFragment by lazy { ProfileFragment() }

    private var activeFragment: Fragment? = null
    // Variable añadida para seguimiento de actividad
    private var lastSelectedFragmentId: Int = R.id.homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Implementación de carga de fragmentos en la actividad principal
        if (savedInstanceState == null) {
            loadFragment(homeFragment)
        }

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

        navigation.selectedItemId = R.id.homeFragment
    }

    private fun loadFragment(fragment: Fragment) {
        if (fragment === activeFragment) {
            return
        }

        val transaction = supportFragmentManager.beginTransaction()

        activeFragment?.let {
            transaction.hide(it)
        }

        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.frame_container, fragment)
        }

        transaction.commit()
        activeFragment = fragment
    }
}

