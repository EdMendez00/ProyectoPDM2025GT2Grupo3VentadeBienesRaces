package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.R
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.PropiedadEntity
import java.io.File
import java.text.NumberFormat
import java.util.Locale

class PropertyAdapter(
    private val propiedades: List<PropiedadEntity>,
    private val onItemClick: ((PropiedadEntity) -> Unit)? = null
) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_property, parent, false)
        return PropertyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = propiedades[position]
        holder.bind(property)
    }

    override fun getItemCount() = propiedades.size

    inner class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_property_title)
        private val tvPrice: TextView = itemView.findViewById(R.id.tv_property_price)
        private val tvLocation: TextView = itemView.findViewById(R.id.tv_property_location)
        private val tvBedroom: TextView = itemView.findViewById(R.id.tv_property_bedroom)
        private val tvBathroom: TextView = itemView.findViewById(R.id.tv_property_bathroom)
        private val tvArea: TextView = itemView.findViewById(R.id.tv_property_area)
        private val imgProperty: ImageView = itemView.findViewById(R.id.iv_property)

        fun bind(property: PropiedadEntity) {
            tvTitle.text = property.titulo

            // Formatear el precio como moneda
            val format = NumberFormat.getCurrencyInstance(Locale.US)
            tvPrice.text = format.format(property.precio)

            tvLocation.text = property.direccion

            // Para características como habitaciones y baños, podríamos extraerlas
            // del campo características si están disponibles
            val caracteristicas = property.caracteristicas.split(",")
            var habitaciones = "N/A"
            var banos = "N/A"

            for (caracteristica in caracteristicas) {
                when {
                    caracteristica.contains("habitacion", ignoreCase = true) -> {
                        habitaciones = caracteristica.trim()
                    }
                    caracteristica.contains("baño", ignoreCase = true) -> {
                        banos = caracteristica.trim()
                    }
                }
            }

            tvBedroom.text = habitaciones
            tvBathroom.text = banos
            tvArea.text = "${property.area} m²"

            // Cargar imagen de la propiedad usando Glide
            loadPropertyImage(property.id, imgProperty)

            // Configurar el clic en el elemento
            itemView.setOnClickListener {
                onItemClick?.invoke(property)
            }
        }

        // Método para cargar la imagen de la propiedad
        private fun loadPropertyImage(propiedadId: Long, imageView: ImageView) {
            // Primero intentamos cargar desde el almacenamiento interno
            try {
                val context = imageView.context
                val filesDir = context.filesDir

                // Buscar archivos que sean imágenes, regresando al comportamiento original
                // pero más ordenado y con mejor manejo de errores
                val possibleFiles = filesDir.listFiles { file ->
                    file.isFile &&
                    (file.name.endsWith(".jpg") ||
                     file.name.endsWith(".jpeg") ||
                     file.name.endsWith(".png"))
                }

                // Si encontramos archivos de imagen, usamos el primero
                // (En una implementación más avanzada, consultaríamos la base de datos local para las rutas específicas)
                if (!possibleFiles.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(possibleFiles[0])
                        .placeholder(R.drawable.placeholder_property)
                        .error(R.drawable.placeholder_property)
                        .centerCrop()
                        .into(imageView)
                    return
                }

                // Si no encontramos imágenes, usar placeholder
                Glide.with(context)
                    .load(R.drawable.placeholder_property)
                    .centerCrop()
                    .into(imageView)

            } catch (e: Exception) {
                // Si hay algún error, cargar el placeholder
                Glide.with(imageView.context)
                    .load(R.drawable.placeholder_property)
                    .centerCrop()
                    .into(imageView)
            }
        }
    }
}
