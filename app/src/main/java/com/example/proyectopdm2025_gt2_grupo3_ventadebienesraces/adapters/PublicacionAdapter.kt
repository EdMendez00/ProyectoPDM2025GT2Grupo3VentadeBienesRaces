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

            // Aquí cargaríamos la imagen usando una biblioteca como Glide o Picasso
            // Por ejemplo:
            // if (propiedad.imagenes.isNotEmpty()) {
            //    Glide.with(itemView.context)
            //        .load(propiedad.imagenes[0])
            //        .placeholder(R.drawable.placeholder_propiedad)
            //        .into(imgPublicacion)
            // }
        }
    }
}
