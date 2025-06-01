package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextFirstName: TextInputEditText
    private lateinit var editTextLastName: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextConfirmPassword: TextInputEditText
    private lateinit var checkBoxTerms: CheckBox
    private lateinit var buttonRegister: Button
    private lateinit var buttonBack: LinearLayout
    private lateinit var textViewLogin: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar Firebase Auth y Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Eliminar verificación de usuario actual si existe
        // No vamos a comprobar si hay usuario autenticado para redirigir automáticamente

        // Inicializar vistas
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        checkBoxTerms = findViewById(R.id.checkBoxTerms)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonBack = findViewById(R.id.btnBack)
        textViewLogin = findViewById(R.id.textViewLogin)

        // Configurar eventos de click
        buttonRegister.setOnClickListener {
            registerUser()
        }

        buttonBack.setOnClickListener {
            finish()
        }

        textViewLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val firstName = editTextFirstName.text.toString().trim()
        val lastName = editTextLastName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()

        // Validar campos
        if (firstName.isEmpty()) {
            editTextFirstName.error = "Ingrese su nombre"
            editTextFirstName.requestFocus()
            return
        }

        if (lastName.isEmpty()) {
            editTextLastName.error = "Ingrese su apellido"
            editTextLastName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            editTextEmail.error = "Ingrese su correo electrónico"
            editTextEmail.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Ingrese un correo electrónico válido"
            editTextEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Ingrese una contraseña"
            editTextPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            editTextPassword.error = "La contraseña debe tener al menos 6 caracteres"
            editTextPassword.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.error = "Confirme su contraseña"
            editTextConfirmPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            editTextConfirmPassword.error = "Las contraseñas no coinciden"
            editTextConfirmPassword.requestFocus()
            return
        }

        if (!checkBoxTerms.isChecked) {
            Toast.makeText(this, "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
            return
        }

        // Mostrar progreso
        buttonRegister.isEnabled = false
        buttonRegister.text = "Creando cuenta..."

        try {
            // Crear usuario en Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Obtener el usuario actual
                        val firebaseUser = auth.currentUser

                        if (firebaseUser != null) {
                            // Mostrar mensaje de éxito inmediatamente
                            Toast.makeText(this, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()

                            // Guardar información adicional en Firestore
                            val user = hashMapOf(
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "email" to email
                            )

                            // IMPORTANTE: Navegamos primero a Home y luego guardamos datos en segundo plano
                            // Esto garantiza que el usuario no quede esperando
                            navigateToHome()

                            // Guardar datos de usuario en segundo plano
                            db.collection("users")
                                .document(firebaseUser.uid)
                                .set(user)
                                .addOnFailureListener { e ->
                                    // Solo registramos el error pero no afecta la navegación
                                    // que ya ocurrió
                                    println("Error guardando datos de usuario: ${e.message}")
                                }
                        } else {
                            // Error: Usuario creado pero no se puede recuperar
                            buttonRegister.isEnabled = true
                            buttonRegister.text = "Crear Cuenta"
                            Toast.makeText(this, "Error: No se pudo acceder al usuario creado", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Error al crear usuario
                        buttonRegister.isEnabled = true
                        buttonRegister.text = "Crear Cuenta"

                        // Determinar el tipo de error
                        val errorMessage = when {
                            task.exception.toString().contains("FirebaseAuthUserCollisionException") ->
                                "El correo electrónico ya está registrado"
                            task.exception.toString().contains("FirebaseNetworkException") ->
                                "Error de conexión. Verifica tu conexión a Internet"
                            else -> "Error al crear la cuenta: ${task.exception?.localizedMessage ?: "Desconocido"}"
                        }

                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            // Capturar cualquier excepción no manejada
            buttonRegister.isEnabled = true
            buttonRegister.text = "Crear Cuenta"
            Toast.makeText(this, "Error inesperado: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para navegar a HomeActivity de manera consistente
    private fun navigateToHome() {
        Intent(this, HomeActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
            finish()
        }
    }
}
