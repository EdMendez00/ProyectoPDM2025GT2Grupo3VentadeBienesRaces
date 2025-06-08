package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.repository.AuthRepository
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewRegister: TextView
    private lateinit var textViewForgotPassword: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var authRepository: AuthRepository

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar Firebase Auth y repository
        auth = FirebaseAuth.getInstance()
        authRepository = AuthRepository(this)

        // Restaurar el token si existe
        authRepository.setupStoredToken()

        // Comprobar si ya hay un token de autenticación
        if (authRepository.getAuthToken() != null) {
            // Si ya tenemos un token, podemos navegar directamente a la pantalla principal
            navigateToHome()
            return
        }

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
            buttonLogin.text = "Iniciando sesión"

            // Llamar a la función para iniciar sesión
            loginUser(email, password)
        }

        // Configurar navegación a registro
        textViewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Configurar recuperación de contraseña
        textViewForgotPassword.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa tu correo para restablecer la contraseña", Toast.LENGTH_SHORT).show()
                editTextEmail.requestFocus()
                return@setOnClickListener
            }
            resetPassword(email)
        }
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Se ha enviado un correo para restablecer tu contraseña", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error al enviar el correo: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso en Firebase
                    Log.d(TAG, "Inicio de sesión exitoso en Firebase")

                    // Guardar el email del usuario para poder identificar sus propiedades
                    val sharedPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    sharedPrefs.edit().putString("user_email", email).apply()

                    // Ahora iniciar sesión en Django para obtener token
                    authenticateWithDjango(email, password)
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

    private fun authenticateWithDjango(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val success = authRepository.loginToDjango(email, password)
                if (success) {
                    // Autenticación exitosa en Django
                    Log.d(TAG, "Autenticación exitosa con backend Django")
                    Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    // Autenticación fallida en Django pero exitosa en Firebase
                    Log.w(TAG, "Autenticación fallida en el backend Django pero exitosa en Firebase")
                    Toast.makeText(
                        this@LoginActivity,
                        "Inicio de sesión parcialmente exitoso, algunas funciones pueden no estar disponibles",
                        Toast.LENGTH_LONG
                    ).show()
                    navigateToHome()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error durante la autenticación con Django", e)
                buttonLogin.isEnabled = true
                buttonLogin.text = "Iniciar Sesión"
                Toast.makeText(
                    this@LoginActivity,
                    "Error de conexión con el servidor",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
