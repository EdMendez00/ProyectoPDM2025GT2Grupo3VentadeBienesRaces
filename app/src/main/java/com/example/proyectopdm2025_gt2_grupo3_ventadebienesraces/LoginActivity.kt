package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewRegister: TextView
    private lateinit var textViewForgotPassword: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

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

            // Mostrar estado del botón
            buttonLogin.isEnabled = false
            buttonLogin.text = "Iniciando sesión..."

            // Iniciar sesión con Firebase
            loginUser(email, password)
        }

        textViewRegister.setOnClickListener {
            // Navegar a la pantalla de registro
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        textViewForgotPassword.setOnClickListener {
            val email = editTextEmail.text.toString().trim()

            if (email.isEmpty()) {
                editTextEmail.error = "Ingresa tu correo electrónico para recuperar tu contraseña"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Se ha enviado un correo para restablecer tu contraseña",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Error al enviar el correo: ${task.exception?.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    // Si falla el inicio de sesión
                    buttonLogin.isEnabled = true
                    buttonLogin.text = "Iniciar Sesión"

                    // Mostrar mensaje de error específico
                    val errorMessage = when {
                        task.exception.toString().contains("FirebaseAuthInvalidUserException") ->
                            "No existe una cuenta con este correo electrónico"
                        task.exception.toString().contains("FirebaseAuthInvalidCredentialsException") ->
                            "Contraseña incorrecta"
                        task.exception.toString().contains("FirebaseNetworkException") ->
                            "Error de conexión. Verifica tu conexión a Internet"
                        else -> "Error al iniciar sesión: ${task.exception?.localizedMessage ?: "Desconocido"}"
                    }

                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToHome() {
        // Navegar a la actividad principal después del login exitoso
        Intent(this, HomeActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
            finish()
        }
    }
}
