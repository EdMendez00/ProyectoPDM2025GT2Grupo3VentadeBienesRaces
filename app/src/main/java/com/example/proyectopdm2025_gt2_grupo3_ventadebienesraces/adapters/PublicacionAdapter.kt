package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.PropiedadDetalleActivity
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.R
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.FavoritosManager
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.model.Propiedad
import java.io.File
import java.text.NumberFormat
import java.util.ArrayList
import java.util.Locale

/**
 * Adaptador para mostrar propiedades en un RecyclerView.
 * @param propiedades Lista de propiedades a mostrar
 * @param onFavoritoClickListener Callback para manejar clics en el botón de favorito
 */
class PublicacionAdapter(
    private val propiedades: List<Propiedad>,
    private val onFavoritoClickListener: ((Propiedad, Boolean) -> Unit)? = null
) : RecyclerView.Adapter<PublicacionAdapter.PropiedadViewHolder>() {

    private lateinit var favoritosManager: FavoritosManager
    private val TAG = "PublicacionAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropiedadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_publicacion, parent, false)

        // Inicializar el FavoritosManager
        if (!::favoritosManager.isInitialized) {
            favoritosManager = FavoritosManager(parent.context)
        }

        return PropiedadViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropiedadViewHolder, position: Int) {
        val propiedad = propiedades[position]
        // Establecer el estado de favorito desde el almacenamiento
        propiedad.esFavorito = favoritosManager.esFavorito(propiedad.id)
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
        private val btnFavorito: ImageView = itemView.findViewById(R.id.btnFavorito)

        fun bind(propiedad: Propiedad) {
            try {
                txtTitulo.text = propiedad.titulo ?: "Sin título"

                // Formato de precio
                val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "SV"))
                txtPrecio.text = formatoMoneda.format(propiedad.precio)

                txtDireccion.text = propiedad.ubicacion.direccion ?: "Dirección no disponible"

                // Usar directamente los campos dormitorios y banos
                val dormitorios = if (propiedad.dormitorios > 0) "${propiedad.dormitorios}" else "N/A"
                val banos = if (propiedad.banos > 0) "${propiedad.banos}" else "N/A"

                txtDormitorios.text = dormitorios
                txtBanos.text = banos
                txtTamano.text = "${propiedad.dimensiones.area} m²"

                // Actualizar estado del botón favorito
                actualizarBotonFavorito(propiedad.esFavorito)

                // Configurar el listener del botón de favorito con manejo de errores
                btnFavorito.setOnClickListener {
                    try {
                        // Cambiar el estado del favorito
                        val nuevoEsFavorito = !propiedad.esFavorito

                        // Actualizar el modelo
                        propiedad.esFavorito = nuevoEsFavorito

                        // Actualizar la UI
                        actualizarBotonFavorito(nuevoEsFavorito)

                        // Guardar el cambio en el almacenamiento local
                        if (nuevoEsFavorito) {
                            // Marcar como favorito
                            val marcado = favoritosManager.marcarComoFavorito(propiedad)
                            Log.d(TAG, "Propiedad marcada como favorita: ${propiedad.id}, resultado: $marcado")
                        } else {
                            // Desmarcar como favorito
                            val desmarcado = favoritosManager.desmarcarComoFavorito(propiedad.id)
                            Log.d(TAG, "Propiedad desmarcada como favorita: ${propiedad.id}, resultado: $desmarcado")
                        }

                        // Notificar al listener externo
                        onFavoritoClickListener?.invoke(propiedad, nuevoEsFavorito)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error al cambiar estado de favorito: ${e.message}", e)
                    }
                }

                // Cargar imagen con Glide
                cargarImagen(propiedad)

                // Configurar OnClickListener para toda la vista del elemento
                configurarClickElemento(propiedad)

            } catch (e: Exception) {
                Log.e(TAG, "Error al vincular datos de propiedad: ${e.message}", e)
            }
        }

        private fun cargarImagen(propiedad: Propiedad) {
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
                        Log.d(TAG, "Cargando imagen desde URL: $imagePath")
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
                            Log.d(TAG, "Cargando imagen local: $imagePath")
                        } else {
                            // Si el archivo no existe, usar placeholder
                            Glide.with(itemView.context)
                                .load(R.drawable.placeholder_home)
                                .centerCrop()
                                .into(imgPublicacion)
                            Log.d(TAG, "Archivo local no encontrado: $imagePath")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error cargando imagen: ${e.message}", e)
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
                Log.d(TAG, "No hay imágenes para esta propiedad")
            }
        }

        private fun configurarClickElemento(propiedad: Propiedad) {
            itemView.setOnClickListener {
                try {
                    // Crear intent para la actividad de detalle
                    val intent = Intent(itemView.context, PropiedadDetalleActivity::class.java).apply {
                        // Pasar todos los datos necesarios
                        putExtra("PROPIEDAD_ID", propiedad.id)
                        putExtra("PROPIEDAD_TITULO", propiedad.titulo)
                        putExtra("PROPIEDAD_DESCRIPCION", propiedad.descripcion)
                        putExtra("PROPIEDAD_PRECIO", propiedad.precio)
                        putExtra("PROPIEDAD_DIRECCION", propiedad.ubicacion.direccion)
                        putExtra("PROPIEDAD_AREA", propiedad.dimensiones.area)
                        putExtra("PROPIEDAD_HABITACIONES", propiedad.dormitorios)
                        putExtra("PROPIEDAD_BANOS", propiedad.banos)
                        putExtra("PROPIEDAD_ESTADO", propiedad.estado)
                        putExtra("PROPIEDAD_CONTACTO", propiedad.medioContacto)
                        putExtra("PROPIEDAD_TIPO", propiedad.tipoPropiedad ?: "N/A")
                        putExtra("PROPIEDAD_ES_FAVORITO", propiedad.esFavorito)

                        // Pasar imagen principal si existe
                        if (propiedad.imagenes.isNotEmpty()) {
                            putExtra("PROPIEDAD_IMAGEN", propiedad.imagenes[0])
                        }

                        // Pasar todas las características como ArrayList
                        putStringArrayListExtra("PROPIEDAD_CARACTERISTICAS", ArrayList(propiedad.caracteristicas))

                        // Datos de vendedor
                        putExtra("VENDEDOR_NOMBRE", propiedad.medioContacto)
                        putExtra("VENDEDOR_EMPRESA", "Empresa Demo")
                        putExtra("VENDEDOR_EMAIL", "contacto@ejemplo.com")
                        // Teléfono para btnLlamar
                        putExtra("PROPIEDAD_TELEFONO", obtenerTelefonoFormateado(propiedad.medioContacto))
                    }

                    // Iniciar la actividad
                    itemView.context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e(TAG, "Error al abrir detalles de propiedad: ${e.message}", e)
                }
            }
        }

        private fun actualizarBotonFavorito(esFavorito: Boolean) {
            if (esFavorito) {
                btnFavorito.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                btnFavorito.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        // Función para formatear el teléfono a xxxx-xxxx
        private fun obtenerTelefonoFormateado(telefono: String): String {
            val soloDigitos = telefono.filter { it.isDigit() }
            return if (soloDigitos.length == 8) {
                soloDigitos.substring(0, 4) + "-" + soloDigitos.substring(4)
            } else telefono
        }
    }
}
