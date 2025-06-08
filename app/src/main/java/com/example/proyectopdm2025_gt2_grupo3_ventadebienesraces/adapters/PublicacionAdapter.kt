package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.R
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
import java.text.NumberFormat
import java.util.Locale
import com.bumptech.glide.Glide
import android.util.Log
import java.io.File

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

            // Mostrar características específicas
            val caracteristicas = propiedad.caracteristicas

            // Buscar dormitorios y baños en las características
            var dormitorios = "N/A"
            var banos = "N/A"

            for (caracteristica in caracteristicas) {
                if (caracteristica.contains("dormitorio", ignoreCase = true)) {
                    dormitorios = caracteristica
                }
                if (caracteristica.contains("baño", ignoreCase = true)) {
                    banos = caracteristica
                }
            }

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
        }
    }
}
