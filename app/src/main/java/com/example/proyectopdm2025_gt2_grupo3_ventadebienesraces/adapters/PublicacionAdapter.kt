package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.PropiedadDetalleActivity
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.R
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
import java.text.NumberFormat
import java.util.Locale
import com.bumptech.glide.Glide
import android.util.Log
import java.io.File
import java.util.ArrayList

class PublicacionAdapter(
    private val propiedades: List<Propiedad>
) : RecyclerView.Adapter<PublicacionAdapter.PropiedadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropiedadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_publicacion, parent, false)
        return PropiedadViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropiedadViewHolder, position: Int) {
        val propiedad = propiedades[position]
        holder.bind(propiedad)
    }

    override fun getItemCount(): Int = propiedades.size

    inner class PropiedadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
        private val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecio)
        private val txtDireccion: TextView = itemView.findViewById(R.id.txtDireccion)
        private val txtDormitorios: TextView = itemView.findViewById(R.id.txtDormitorios)
        private val txtBanos: TextView = itemView.findViewById(R.id.txtBanos)
        private val txtTamano: TextView = itemView.findViewById(R.id.txtTamano)
        private val imgPublicacion: ImageView = itemView.findViewById(R.id.imgPublicacion)

        fun bind(propiedad: Propiedad) {
            txtTitulo.text = propiedad.titulo

            // Formato de precio
            val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "SV"))
            txtPrecio.text = formatoMoneda.format(propiedad.precio)

            txtDireccion.text = propiedad.ubicacion.direccion

            // Analizar las características para encontrar dormitorios y baños
            var dormitorios = "N/A"
            var banos = "N/A"

            // Revisar todas las características para encontrar patrones
            for (caracteristica in propiedad.caracteristicas) {
                // Buscar patrones para dormitorios/habitaciones
                if (caracteristica.contains("dormitorio", ignoreCase = true) ||
                    caracteristica.contains("habitacion", ignoreCase = true) ||
                    caracteristica.contains("habitación", ignoreCase = true) ||
                    caracteristica.contains("dorm", ignoreCase = true) ||
                    caracteristica.matches(Regex("\\d+\\s*dormitorio.*", RegexOption.IGNORE_CASE)) ||
                    caracteristica.matches(Regex("\\d+\\s*hab.*", RegexOption.IGNORE_CASE))) {
                    dormitorios = caracteristica
                    continue
                }

                // Si hay un número simple que podría ser número de dormitorios
                if (dormitorios == "N/A" && caracteristica.trim().matches(Regex("\\d+"))) {
                    // Si es el primer número que encontramos, asumimos dormitorios
                    if (banos == "N/A") {
                        dormitorios = "${caracteristica.trim()} dormitorios"
                        continue
                    }
                }

                // Buscar patrones para baños
                if (caracteristica.contains("baño", ignoreCase = true) ||
                    caracteristica.contains("bano", ignoreCase = true) ||
                    caracteristica.contains("bath", ignoreCase = true) ||
                    caracteristica.matches(Regex("\\d+\\s*baño.*", RegexOption.IGNORE_CASE)) ||
                    caracteristica.matches(Regex("\\d+\\s*ban.*", RegexOption.IGNORE_CASE))) {
                    banos = caracteristica
                    continue
                }
            }

            // Última alternativa: buscar en la descripción
            if (dormitorios == "N/A" || banos == "N/A") {
                // Buscar patrones como "3 dormitorios, 2 baños" en la descripción
                val dormitoriosPattern = Regex("(\\d+)\\s*(?:dormitorio|habitación|habitacion|dorm|hab)s?", RegexOption.IGNORE_CASE)
                val banosPattern = Regex("(\\d+)\\s*(?:baño|bano|bath)s?", RegexOption.IGNORE_CASE)

                val dormitoriosMatch = dormitoriosPattern.find(propiedad.descripcion)
                val banosMatch = banosPattern.find(propiedad.descripcion)

                if (dormitoriosMatch != null && dormitorios == "N/A") {
                    dormitorios = "${dormitoriosMatch.groupValues[1]} dormitorios"
                }

                if (banosMatch != null && banos == "N/A") {
                    banos = "${banosMatch.groupValues[1]} baños"
                }
            }

            // Asignar los valores encontrados
            txtDormitorios.text = dormitorios
            txtBanos.text = banos
            txtTamano.text = "${propiedad.dimensiones.area} m²"

            // Cargar imagen con Glide
            if (propiedad.imagenes.isNotEmpty()) {
                val imagePath = propiedad.imagenes[0]

                try {
                    if (imagePath.startsWith("http")) {
                        // Es una URL web
                        Glide.with(itemView.context)
                            .load(imagePath)
                            .placeholder(R.drawable.placeholder_home)
                            .error(R.drawable.placeholder_home)
                            .centerCrop()
                            .into(imgPublicacion)
                        Log.d("PublicacionAdapter", "Cargando imagen desde URL: $imagePath")
                    } else {
                        // Es una ruta de archivo local
                        val imageFile = File(imagePath)
                        if (imageFile.exists()) {
                            Glide.with(itemView.context)
                                .load(imageFile)
                                .placeholder(R.drawable.placeholder_home)
                                .error(R.drawable.placeholder_home)
                                .centerCrop()
                                .into(imgPublicacion)
                            Log.d("PublicacionAdapter", "Cargando imagen local: $imagePath")
                        } else {
                            // Si el archivo no existe, usar placeholder
                            Glide.with(itemView.context)
                                .load(R.drawable.placeholder_home)
                                .centerCrop()
                                .into(imgPublicacion)
                            Log.d("PublicacionAdapter", "Archivo local no encontrado: $imagePath")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("PublicacionAdapter", "Error cargando imagen: ${e.message}", e)
                    // Cargar imagen por defecto en caso de error
                    Glide.with(itemView.context)
                        .load(R.drawable.placeholder_home)
                        .centerCrop()
                        .into(imgPublicacion)
                }
            } else {
                // Si no hay imágenes, usar placeholder
                Glide.with(itemView.context)
                    .load(R.drawable.placeholder_home)
                    .centerCrop()
                    .into(imgPublicacion)
                Log.d("PublicacionAdapter", "No hay imágenes para esta propiedad")
            }

            // Configurar OnClickListener para toda la vista del elemento
            itemView.setOnClickListener {
                // Crear intent para la actividad de detalle
                val intent = Intent(itemView.context, PropiedadDetalleActivity::class.java).apply {
                    // Pasar todos los datos necesarios
                    putExtra("PROPIEDAD_ID", propiedad.id)
                    putExtra("PROPIEDAD_TITULO", propiedad.titulo)
                    putExtra("PROPIEDAD_DESCRIPCION", propiedad.descripcion)
                    putExtra("PROPIEDAD_PRECIO", propiedad.precio)
                    putExtra("PROPIEDAD_DIRECCION", propiedad.ubicacion.direccion)
                    putExtra("PROPIEDAD_AREA", propiedad.dimensiones.area)
                    putExtra("PROPIEDAD_HABITACIONES", dormitorios)
                    putExtra("PROPIEDAD_BANOS", banos)
                    putExtra("PROPIEDAD_ESTADO", propiedad.estado)
                    putExtra("PROPIEDAD_CONTACTO", propiedad.medioContacto)

                    // Pasar imagen principal si existe
                    if (propiedad.imagenes.isNotEmpty()) {
                        putExtra("PROPIEDAD_IMAGEN", propiedad.imagenes[0])
                    }

                    // Pasar todas las características como ArrayList
                    putStringArrayListExtra("PROPIEDAD_CARACTERISTICAS", ArrayList(propiedad.caracteristicas))
                }

                // Iniciar la actividad
                itemView.context.startActivity(intent)
            }
        }
    }
}
