package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewRegister: TextView
    private lateinit var textViewForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar vistas
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewRegister = findViewById(R.id.textViewRegister)
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword)

        // Configurar eventos de click
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // Validar campos
            if (email.isEmpty()) {
                editTextEmail.error = "Ingresa tu correo electrónico"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                editTextPassword.error = "Ingresa tu contraseña"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            // Para propósitos de demostración, iniciar sesión con cualquier credencial
            // En una implementación real, aquí se haría la verificación con una API o base de datos
            loginUser(email, password)
        }

        textViewRegister.setOnClickListener {
            // Navegar a la pantalla de registro
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        textViewForgotPassword.setOnClickListener {
            // Aquí se implementará la funcionalidad de recuperación de contraseña
            Toast.makeText(this, "Funcionalidad de recuperación de contraseña próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginUser(email: String, password: String) {
        // Para demostración, simularemos un login exitoso
        // En una app real, conectarías con una API o Firebase Authentication

        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

        // Navegar a la actividad principal después del login exitoso
        Intent(this, HomeActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
            finish()
        }
    }
}
