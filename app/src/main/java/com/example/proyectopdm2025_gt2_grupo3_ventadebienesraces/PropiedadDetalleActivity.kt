package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
import java.io.File
import java.text.NumberFormat
import java.util.Locale

class PropiedadDetalleActivity : AppCompatActivity() {

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
        val tvEstado = findViewById<TextView>(R.id.tvEstado)
        val tvUbicacion = findViewById<TextView>(R.id.tvUbicacion)
        val tvDescripcion = findViewById<TextView>(R.id.tvDescripcion)
        val tvDormitorios = findViewById<TextView>(R.id.tvDormitorios)
        val tvBanos = findViewById<TextView>(R.id.tvBanos)
        val tvArea = findViewById<TextView>(R.id.tvArea)
        val tvCaracteristicas = findViewById<TextView>(R.id.tvCaracteristicas)
        val tvContacto = findViewById<TextView>(R.id.tvContacto)
        val btnContactar = findViewById<Button>(R.id.btnContactar)

        // Configurar el botón de retroceso
        btnBack.setOnClickListener {
            finish()
        }

        // Mostrar la información de la propiedad
        tvTitulo.text = titulo
        tvDescripcion.text = descripcion

        // Formatear precio
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "SV"))
        tvPrecio.text = formatoMoneda.format(precio)

        tvUbicacion.text = direccion
        tvDormitorios.text = habitaciones
        tvBanos.text = banos
        tvArea.text = "$area m²"

        // Configurar estado
        tvEstado.text = estado
        tvEstado.setBackgroundResource(
            if (estado == "DISPONIBLE") android.R.color.holo_green_dark
            else android.R.color.holo_red_light
        )

        // Mostrar características como lista
        val caracteristicasTexto = caracteristicas.joinToString("\n") { "• $it" }
        tvCaracteristicas.text = caracteristicasTexto

        // Mostrar información de contacto
        tvContacto.text = contacto

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

        // Configurar botón de contactar (extraer número de teléfono del contacto)
        btnContactar.setOnClickListener {
            // Intentar extraer un número telefónico del texto de contacto
            val phoneRegex = Regex("\\d{4}[-\\s]?\\d{4}")
            val phoneMatch = phoneRegex.find(contacto)

            if (phoneMatch != null) {
                // Si se encontró un teléfono, iniciar intent para llamar
                val phoneNumber = phoneMatch.value.replace("-", "").replace(" ", "")
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:$phoneNumber")

                if (dialIntent.resolveActivity(packageManager) != null) {
                    startActivity(dialIntent)
                }
            } else {
                // Si no se encontró teléfono, mostrar el contacto completo
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Contacto para: $titulo")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, contacto)

                startActivity(Intent.createChooser(sharingIntent, "Contactar mediante"))
            }
        }
    }
}
