package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.R
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad

class PublicacionAdapter(private val publicaciones: List<Propiedad>) : RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder>() {

    class PublicacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPublicacion: ImageView = itemView.findViewById(R.id.imgPublicacion)
        val txtTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val txtDireccion: TextView = itemView.findViewById(R.id.txtDireccion)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecio)
        val txtDormitorios: TextView = itemView.findViewById(R.id.txtDormitorios)
        val txtBanos: TextView = itemView.findViewById(R.id.txtBanos)
        val txtTamano: TextView = itemView.findViewById(R.id.txtTamano)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_publicacion, parent, false)
        return PublicacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PublicacionViewHolder, position: Int) {
        val propiedad = publicaciones[position]
        // Cargar la primera imagen si existe
        val imagenUrl = propiedad.imagenes.firstOrNull()
        Glide.with(holder.itemView.context)
            .load(imagenUrl)
            .placeholder(R.drawable.ic_location)
            .into(holder.imgPublicacion)
        holder.txtTitulo.text = propiedad.titulo
        // Mostrar ubicación y dirección
        holder.txtDireccion.text = "${propiedad.ubicacion.direccion}"
        // Mostrar precio con formato
        holder.txtPrecio.text = "$${String.format("%,.2f", propiedad.precio)}"
        // Características: buscar dormitorios y baños en la lista de características
        val dormitorios = propiedad.caracteristicas.find { it.contains("dormitorio", true) }?.replace(Regex("[^0-9]"), "") ?: "-"
        val banos = propiedad.caracteristicas.find { it.contains("baño", true) || it.contains("bano", true) }?.replace(Regex("[^0-9]"), "") ?: "-"
        holder.txtDormitorios.text = dormitorios
        holder.txtBanos.text = banos
        // Tamaño del terreno
        holder.txtTamano.text = if (propiedad.dimensiones.area > 0) "${propiedad.dimensiones.area} m²" else "-"
    }

    override fun getItemCount(): Int = publicaciones.size
}
