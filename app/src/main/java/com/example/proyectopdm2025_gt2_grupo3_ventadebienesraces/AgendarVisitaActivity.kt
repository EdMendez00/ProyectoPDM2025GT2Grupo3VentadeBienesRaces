package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AgendarVisitaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_agendar_visita)

        // Aquí puedes pasar los datos al fragmento si lo necesitas
        val fragment = AgendarVisitaFragment()
        fragment.arguments = intent.extras
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit()
    }
}

