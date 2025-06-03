package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters.FeatureAdapter
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.ImagenPropiedadEntity
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.local.entity.PropiedadEntity
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.viewmodel.PropiedadViewModel
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AddFragment : Fragment() {
    // ViewModel
    private lateinit var propiedadViewModel: PropiedadViewModel

    // UI Components
    private lateinit var etTitle: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var etPrice: TextInputEditText
    private lateinit var actvPropertyType: AutoCompleteTextView
    private lateinit var etBedrooms: TextInputEditText
    private lateinit var etBathrooms: TextInputEditText
    private lateinit var etArea: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var actvMunicipio: AutoCompleteTextView
    private lateinit var actvDepartamento: AutoCompleteTextView
    private lateinit var btnUploadImage: Button
    private lateinit var layoutImagePreviews: LinearLayout
    private lateinit var etFeature: TextInputEditText
    private lateinit var btnAddFeature: Button
    private lateinit var tvNoFeatures: TextView
    private lateinit var rvFeatures: RecyclerView
    private lateinit var etContactName: TextInputEditText
    private lateinit var etPhoneNumber: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etCompany: TextInputEditText
    private lateinit var btnPublish: Button

    private val imageUris = mutableListOf<Uri>()
    private val features = mutableListOf<String>()
    private lateinit var featureAdapter: FeatureAdapter

    // Launcher para seleccionar imágenes
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val selectedImageUri = result.data?.data
            selectedImageUri?.let {
                if (imageUris.size < 5) {
                    imageUris.add(it)
                    addImagePreview(it)
                } else {
                    Toast.makeText(requireContext(), "Máximo 5 imágenes permitidas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar ViewModel
        propiedadViewModel = ViewModelProvider(this).get(PropiedadViewModel::class.java)

        // Inicializar vistas
        initViews(view)
        setupSpinners()
        setupRecyclerView()
        setupListeners()
    }

    private fun initViews(view: View) {
        etTitle = view.findViewById(R.id.etTitle)
        etDescription = view.findViewById(R.id.etDescription)
        etPrice = view.findViewById(R.id.etPrice)
        actvPropertyType = view.findViewById(R.id.actvPropertyType)
        etBedrooms = view.findViewById(R.id.etBedrooms)
        etBathrooms = view.findViewById(R.id.etBathrooms)
        etArea = view.findViewById(R.id.etArea)
        etAddress = view.findViewById(R.id.etAddress)
        actvMunicipio = view.findViewById(R.id.actvMunicipio)
        actvDepartamento = view.findViewById(R.id.actvDepartamento)
        btnUploadImage = view.findViewById(R.id.btnUploadImage)
        layoutImagePreviews = view.findViewById(R.id.layoutImagePreviews)
        etFeature = view.findViewById(R.id.etFeature)
        btnAddFeature = view.findViewById(R.id.btnAddFeature)
        tvNoFeatures = view.findViewById(R.id.tvNoFeatures)
        rvFeatures = view.findViewById(R.id.rvFeatures)
        etContactName = view.findViewById(R.id.etContactName)
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber)
        etEmail = view.findViewById(R.id.etEmail)
        etCompany = view.findViewById(R.id.etCompany)
        btnPublish = view.findViewById(R.id.btnPublish)
    }

    private fun setupSpinners() {
        // Configurar spinner de tipos de propiedad
        val propertyTypes = arrayOf("Casa", "Apartamento", "Terreno", "Local comercial", "Oficina")
        val propertyTypeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, propertyTypes)
        actvPropertyType.setAdapter(propertyTypeAdapter)

        // Configurar spinner de municipios
        val municipios = arrayOf("San Salvador", "Santa Ana", "San Miguel", "Sonsonate", "La Libertad")
        val municipioAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, municipios)
        actvMunicipio.setAdapter(municipioAdapter)

        // Configurar spinner de departamentos
        val departamentos = arrayOf("San Salvador", "Santa Ana", "San Miguel", "Sonsonate", "La Libertad", "Ahuachapán", "Cabañas", "Chalatenango", "Cuscatlán", "Morazán", "La Paz", "La Unión", "San Vicente", "Usulután")
        val departamentoAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, departamentos)
        actvDepartamento.setAdapter(departamentoAdapter)
    }

    private fun setupRecyclerView() {
        featureAdapter = FeatureAdapter(features) { position ->
            // Callback para eliminar característica
            features.removeAt(position)
            featureAdapter.notifyItemRemoved(position)
            updateFeaturesVisibility()
        }
        rvFeatures.layoutManager = LinearLayoutManager(requireContext())
        rvFeatures.adapter = featureAdapter
    }

    private fun setupListeners() {
        // Botón para subir imágenes
        btnUploadImage.setOnClickListener {
            openImagePicker()
        }

        // Botón para agregar características
        btnAddFeature.setOnClickListener {
            val feature = etFeature.text.toString().trim()
            if (feature.isNotEmpty()) {
                features.add(feature)
                featureAdapter.notifyItemInserted(features.size - 1)
                etFeature.setText("")
                updateFeaturesVisibility()
            } else {
                Toast.makeText(requireContext(), "Ingrese una característica", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para publicar
        btnPublish.setOnClickListener {
            if (validateInputs()) {
                saveProperty()
            }
        }

        // Botón para volver atrás
        view?.findViewById<ImageButton>(R.id.btnBack)?.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun addImagePreview(imageUri: Uri) {
        val imagePreviewContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size),
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(4, 4, 4, 4)
        }

        // Crear CardView para la imagen
        val cardView = CardView(requireContext()).apply {
            radius = resources.getDimension(com.google.android.material.R.dimen.cardview_default_radius)
            cardElevation = resources.getDimension(com.google.android.material.R.dimen.cardview_default_elevation)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        // Imagen
        val imageView = ImageView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageURI(imageUri)
        }

        // Botón para eliminar la imagen
        val deleteButton = ImageButton(requireContext()).apply {
            setImageResource(android.R.drawable.ic_delete)
            setBackgroundResource(android.R.color.transparent)
            setPadding(4, 4, 4, 4)
            setOnClickListener {
                val index = layoutImagePreviews.indexOfChild(imagePreviewContainer)
                if (index >= 0 && index < imageUris.size) {
                    imageUris.removeAt(index)
                    layoutImagePreviews.removeView(imagePreviewContainer)
                }
            }
        }

        cardView.addView(imageView)
        imagePreviewContainer.addView(cardView)
        imagePreviewContainer.addView(deleteButton)

        layoutImagePreviews.addView(imagePreviewContainer)
    }

    private fun updateFeaturesVisibility() {
        if (features.isEmpty()) {
            tvNoFeatures.visibility = View.VISIBLE
            rvFeatures.visibility = View.GONE
        } else {
            tvNoFeatures.visibility = View.GONE
            rvFeatures.visibility = View.VISIBLE
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validar título
        if (etTitle.text.toString().trim().isEmpty()) {
            etTitle.error = "El título es obligatorio"
            isValid = false
        }

        // Validar precio
        if (etPrice.text.toString().trim().isEmpty()) {
            etPrice.error = "El precio es obligatorio"
            isValid = false
        }

        // Validar dirección
        if (etAddress.text.toString().trim().isEmpty()) {
            etAddress.error = "La dirección es obligatoria"
            isValid = false
        }

        // Validar nombre de contacto
        if (etContactName.text.toString().trim().isEmpty()) {
            etContactName.error = "El nombre de contacto es obligatorio"
            isValid = false
        }

        // Validar teléfono
        if (etPhoneNumber.text.toString().trim().isEmpty()) {
            etPhoneNumber.error = "El teléfono es obligatorio"
            isValid = false
        }

        return isValid
    }

    private fun saveProperty() {
        try {
            // Muestra un diálogo de progreso
            val progressDialog = android.app.ProgressDialog(requireContext())
            progressDialog.setMessage("Guardando propiedad...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            // Obtener el ID del usuario actual (vendedor) - Por ahora usamos un ID de ejemplo
            val vendedorId = "user_" + UUID.randomUUID().toString().substring(0, 8)

            // Recolectar valores seguros (con validación para evitar NullPointerException)
            val titulo = etTitle.text?.toString()?.trim() ?: ""
            val descripcion = etDescription.text?.toString()?.trim() ?: ""
            val precio = try {
                etPrice.text?.toString()?.toDoubleOrNull() ?: 0.0
            } catch (e: Exception) {
                0.0
            }
            val direccion = etAddress.text?.toString()?.trim() ?: ""
            val municipio = actvMunicipio.text?.toString() ?: ""
            val departamento = actvDepartamento.text?.toString() ?: ""
            val area = try {
                etArea.text?.toString()?.toDoubleOrNull() ?: 0.0
            } catch (e: Exception) {
                0.0
            }
            val contactName = etContactName.text?.toString()?.trim() ?: ""
            val phone = etPhoneNumber.text?.toString()?.trim() ?: ""
            val email = etEmail.text?.toString()?.trim() ?: ""
            val company = etCompany.text?.toString()?.trim() ?: ""

            // Crear la dirección completa de forma segura
            val direccionCompleta = StringBuilder().apply {
                append(direccion)
                if (municipio.isNotEmpty()) {
                    append(", ")
                    append(municipio)
                }
                if (departamento.isNotEmpty()) {
                    append(", ")
                    append(departamento)
                }
            }.toString()

            // Crear objeto PropiedadEntity de forma segura
            val propiedad = PropiedadEntity(
                titulo = titulo,
                descripcion = descripcion,
                precio = precio,
                direccion = direccionCompleta,
                latitud = 0.0,
                longitud = 0.0,
                largo = 0.0,
                ancho = 0.0,
                area = area,
                caracteristicas = features.joinToString(","),
                vendedorId = vendedorId,
                fechaPublicacion = Date(),
                estado = "DISPONIBLE",
                medioContacto = "Nombre: $contactName, Teléfono: $phone, Email: $email" +
                        if (company.isNotEmpty()) ", Empresa: $company" else ""
            )

            // Crear un intent para verificar que la actividad de destino existe
            // (esto previene el crash si no existe)
            val intentTarget = Intent(activity, HomeActivity::class.java)

            // Guardar propiedad en la base de datos con manejo de errores
            propiedadViewModel.insertPropiedad(propiedad) { propiedadId ->
                try {
                    // Guardar imágenes
                    if (imageUris.isNotEmpty()) {
                        saveImages(propiedadId)
                    }

                    // Ocultar el diálogo de progreso
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }

                    // Ejecutar en el hilo principal
                    activity?.runOnUiThread {
                        try {
                            // Mostrar mensaje de éxito
                            Toast.makeText(context, "Propiedad publicada con éxito", Toast.LENGTH_LONG).show()

                            // Limpiar el formulario
                            clearForm()

                            // NAVEGACIÓN SEGURA: Navegar a la actividad principal con un flag específico para ir a "Mis publicaciones"
                            val intent = Intent(activity, HomeActivity::class.java).apply {
                                putExtra("NAVIGATE_TO", "MIS_PUBLICACIONES")
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            }

                            startActivity(intent)
                        } catch (e: Exception) {
                            Log.e("AddFragment", "Error en la navegación: ${e.message}", e)
                            Toast.makeText(context, "La publicación se guardó correctamente", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }

                    Log.e("AddFragment", "Error al guardar imágenes: ${e.message}", e)

                    activity?.runOnUiThread {
                        Toast.makeText(context, "Error al guardar imágenes: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("AddFragment", "Error principal al guardar: ${e.message}", e)
            Toast.makeText(context, "Error al guardar la propiedad. Inténtalo de nuevo", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveImages(propiedadId: Long) {
        // Guardar las imágenes en el almacenamiento interno
        for (uri in imageUris) {
            try {
                // Crear nombre único para el archivo
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val imageName = "IMG_${timestamp}_${UUID.randomUUID().toString().substring(0, 8)}.jpg"

                // Guardar imagen en el almacenamiento interno
                val imageFile = File(requireContext().filesDir, imageName)
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(imageFile)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()

                // Crear objeto ImagenPropiedadEntity
                val imagen = ImagenPropiedadEntity(
                    propiedadId = propiedadId,
                    rutaImagen = imageFile.absolutePath
                )

                // Guardar referencia en la base de datos
                propiedadViewModel.insertImagen(imagen)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun clearForm() {
        etTitle.setText("")
        etDescription.setText("")
        etPrice.setText("")
        actvPropertyType.setText("")
        etBedrooms.setText("")
        etBathrooms.setText("")
        etArea.setText("")
        etAddress.setText("")
        actvMunicipio.setText("")
        actvDepartamento.setText("")
        etContactName.setText("")
        etPhoneNumber.setText("")
        etEmail.setText("")
        etCompany.setText("")

        // Limpiar imágenes
        imageUris.clear()
        layoutImagePreviews.removeAllViews()

        // Limpiar características
        features.clear()
        featureAdapter.notifyDataSetChanged()
        updateFeaturesVisibility()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddFragment()
    }
}
