package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import java.io.File
import java.text.NumberFormat
import java.util.Locale

class PropiedadDetalleActivity : AppCompatActivity() {

    private lateinit var layoutDetalles: LinearLayout
    private lateinit var layoutCaracteristicas: LinearLayout
    private lateinit var layoutContacto: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_propiedad)

        // Obtener datos de la propiedad del intent
        val propiedadId = intent.getStringExtra("PROPIEDAD_ID") ?: ""
        val titulo = intent.getStringExtra("PROPIEDAD_TITULO") ?: ""
        val descripcion = intent.getStringExtra("PROPIEDAD_DESCRIPCION") ?: ""
        val precio = intent.getDoubleExtra("PROPIEDAD_PRECIO", 0.0)
        val direccion = intent.getStringExtra("PROPIEDAD_DIRECCION") ?: ""
        val area = intent.getDoubleExtra("PROPIEDAD_AREA", 0.0)
        val habitaciones = intent.getStringExtra("PROPIEDAD_HABITACIONES") ?: "N/A"
        val banos = intent.getStringExtra("PROPIEDAD_BANOS") ?: "N/A"
        val estado = intent.getStringExtra("PROPIEDAD_ESTADO") ?: "DISPONIBLE"
        val contacto = intent.getStringExtra("PROPIEDAD_CONTACTO") ?: ""
        val imageUrl = intent.getStringExtra("PROPIEDAD_IMAGEN") ?: ""

        // Lista de características
        val caracteristicas = intent.getStringArrayListExtra("PROPIEDAD_CARACTERISTICAS") ?: ArrayList()

        // Referencias a las vistas
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val imgPropiedad = findViewById<ImageView>(R.id.imgPropiedad)
        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        val tvPrecio = findViewById<TextView>(R.id.tvPrecio)
        val tvUbicacion = findViewById<TextView>(R.id.tvUbicacion)
        val tvDescripcion = findViewById<TextView>(R.id.tvDescripcion)
        val tvDormitorios = findViewById<TextView>(R.id.tvDormitorios)
        val tvBanos = findViewById<TextView>(R.id.tvBanos)
        val tvArea = findViewById<TextView>(R.id.tvArea)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val tvEstado = findViewById<TextView>(R.id.tvEstado)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Referencias a los layouts de cada sección
        layoutDetalles = findViewById(R.id.layoutDetalles)
        layoutCaracteristicas = findViewById(R.id.layoutCaracteristicas)
        layoutContacto = findViewById(R.id.layoutContacto)

        // Configurar el botón de retroceso
        btnBack.setOnClickListener {
            finish()
        }

        // Configurar la barra de navegación inferior
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    // Ir a la pantalla de inicio
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    true
                }
                R.id.searchFragment -> {
                    // Ir a la pantalla de búsqueda
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("FRAGMENT_TO_SHOW", "search")
                    startActivity(intent)
                    true
                }
                R.id.addFragment -> {
                    // Ir a pantalla de agregar propiedad
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("FRAGMENT_TO_SHOW", "add")
                    startActivity(intent)
                    true
                }
                R.id.favoriteFragment -> {
                    // Ir a favoritos
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("FRAGMENT_TO_SHOW", "favorites")
                    startActivity(intent)
                    true
                }
                R.id.profileFragment -> {
                    // Ir al perfil
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("FRAGMENT_TO_SHOW", "profile")
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Configurar el TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mostrarSeccion(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // Mostrar la información de la propiedad
        tvTitulo.text = titulo
        tvDescripcion.text = descripcion
        tvEstado.text = estado

        // Formatear precio
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "SV"))
        tvPrecio.text = formatoMoneda.format(precio)

        tvUbicacion.text = direccion
        tvDormitorios.text = habitaciones
        tvBanos.text = banos
        tvArea.text = "$area m²"

        // Agregar las características dinámicamente a la sección de características
        if (caracteristicas.isNotEmpty()) {
            // Aquí podrías agregar código para añadir características dinámicamente a la sección
            // utilizando el patrón de diseño que tienes en tu XML
        }

        // Cargar imagen con Glide
        if (imageUrl.isNotEmpty()) {
            try {
                if (imageUrl.startsWith("http")) {
                    // Es una URL web
                    Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_home)
                        .error(R.drawable.placeholder_home)
                        .centerCrop()
                        .into(imgPropiedad)
                } else {
                    // Es una ruta de archivo local
                    val imageFile = File(imageUrl)
                    if (imageFile.exists()) {
                        Glide.with(this)
                            .load(imageFile)
                            .placeholder(R.drawable.placeholder_home)
                            .error(R.drawable.placeholder_home)
                            .centerCrop()
                            .into(imgPropiedad)
                    } else {
                        // Si el archivo no existe, usar placeholder
                        Glide.with(this)
                            .load(R.drawable.placeholder_home)
                            .centerCrop()
                            .into(imgPropiedad)
                    }
                }
            } catch (e: Exception) {
                // Cargar imagen por defecto en caso de error
                Glide.with(this)
                    .load(R.drawable.placeholder_home)
                    .centerCrop()
                    .into(imgPropiedad)
            }
        } else {
            // Si no hay imagen, usar placeholder
            Glide.with(this)
                .load(R.drawable.placeholder_home)
                .centerCrop()
                .into(imgPropiedad)
        }

        // Por defecto, mostrar la sección de detalles
        mostrarSeccion(0)
    }

    private fun mostrarSeccion(position: Int) {
        // Ocultar todas las secciones
        layoutDetalles.visibility = View.GONE
        layoutCaracteristicas.visibility = View.GONE
        layoutContacto.visibility = View.GONE

        // Mostrar la sección seleccionada
        when (position) {
            0 -> layoutDetalles.visibility = View.VISIBLE
            1 -> layoutCaracteristicas.visibility = View.VISIBLE
            2 -> layoutContacto.visibility = View.VISIBLE
        }
    }
}
