package com.example.proyectopdm2025gt2grupo3ventadebienesraces;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.R;

public class AddFragment extends Fragment {
    private static final String TAG = "AddFragment";

    // Variables para las vistas
    private EditText etTitle, etDescription, etPrice, etBedrooms, etBathrooms, etArea;
    private EditText etAddress, etFeature, etContactName, etPhoneNumber, etEmail, etCompany;
    private Button btnUploadImage, btnAddFeature, btnPublish;
    private AutoCompleteTextView actvPropertyType, actvDepartamento, actvMunicipio;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        
        // Inicializar vistas
        inicializarVistas(view);
        
        // Configurar spinners con datos
        configurarSpinners();
        
        // Configurar botones
        configurarBotones();
        
        return view;
    }
    
    private void inicializarVistas(View view) {
        try {
            // Campos de texto
            etTitle = view.findViewById(R.id.etTitle);
            etDescription = view.findViewById(R.id.etDescription);
            etPrice = view.findViewById(R.id.etPrice);
            etBedrooms = view.findViewById(R.id.etBedrooms);
            etBathrooms = view.findViewById(R.id.etBathrooms);
            etArea = view.findViewById(R.id.etArea);
            etAddress = view.findViewById(R.id.etAddress);
            etFeature = view.findViewById(R.id.etFeature);
            etContactName = view.findViewById(R.id.etContactName);
            etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
            etEmail = view.findViewById(R.id.etEmail);
            etCompany = view.findViewById(R.id.etCompany);
            
            // Spinners (AutoCompleteTextView)
            actvPropertyType = view.findViewById(R.id.actvPropertyType);
            actvDepartamento = view.findViewById(R.id.actvDepartamento);
            actvMunicipio = view.findViewById(R.id.actvMunicipio);
            
            // Botones
            btnUploadImage = view.findViewById(R.id.btnUploadImage);
            btnAddFeature = view.findViewById(R.id.btnAddFeature);
            btnPublish = view.findViewById(R.id.btnPublish);
        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar vistas: " + e.getMessage());
            Toast.makeText(requireContext(), "Error al inicializar la interfaz", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void configurarSpinners() {
        try {
            // Configurar adapter para tipo de propiedad
            String[] tiposPropiedad = {"Casa", "Apartamento", "Local comercial", "Terreno", "Oficina"};
            ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    tiposPropiedad);
            actvPropertyType.setAdapter(adapterTipos);
            
            // Configurar adapter para departamentos
            String[] departamentos = {"San Salvador", "La Libertad", "Santa Ana", "San Miguel", "Sonsonate"};
            ArrayAdapter<String> adapterDepartamentos = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    departamentos);
            actvDepartamento.setAdapter(adapterDepartamentos);
            
            // Configurar adapter para municipios (ajustar según el departamento seleccionado)
            String[] municipios = {"San Salvador", "Mejicanos", "Soyapango", "Apopa", "Santa Tecla"};
            ArrayAdapter<String> adapterMunicipios = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    municipios);
            actvMunicipio.setAdapter(adapterMunicipios);
        } catch (Exception e) {
            Log.e(TAG, "Error al configurar spinners: " + e.getMessage());
            Toast.makeText(requireContext(), "Error al configurar los menús desplegables", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void configurarBotones() {
        // Botón para subir imágenes
        btnUploadImage.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Funcionalidad de subir imagen no implementada", Toast.LENGTH_SHORT).show();
            // Implementar la selección de imágenes aquí
        });
        
        // Botón para añadir características
        btnAddFeature.setOnClickListener(v -> {
            String feature = etFeature.getText().toString().trim();
            if (!feature.isEmpty()) {
                Toast.makeText(requireContext(), "Característica agregada: " + feature, Toast.LENGTH_SHORT).show();
                etFeature.setText("");
                // Añadir la característica a la lista aquí
            }
        });
        
        // Botón para publicar
        btnPublish.setOnClickListener(v -> {
            if (validarCampos()) {
                enviarDatosADetalle();
            }
        });
    }
    
    private boolean validarCampos() {
        // Validación básica de campos requeridos
        if (etTitle.getText().toString().trim().isEmpty()) {
            mostrarError("Ingrese un título para la propiedad");
            return false;
        }
        
        if (etDescription.getText().toString().trim().isEmpty()) {
            mostrarError("Ingrese una descripción para la propiedad");
            return false;
        }
        
        if (etPrice.getText().toString().trim().isEmpty()) {
            mostrarError("Ingrese un precio para la propiedad");
            return false;
        }
        
        if (actvPropertyType.getText().toString().trim().isEmpty()) {
            mostrarError("Seleccione un tipo de propiedad");
            return false;
        }
        
        // Si todos los campos requeridos están completos
        return true;
    }
    
    private void mostrarError(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
    
    private void enviarDatosADetalle() {
        try {
            // Crear un Intent para la actividad de detalle de propiedad
            Intent intent = new Intent(requireContext(), DetallePropiedad.class);
            
            // Añadir los datos al intent
            intent.putExtra("titulo", etTitle.getText().toString().trim());
            intent.putExtra("descripcion", etDescription.getText().toString().trim());
            intent.putExtra("precio", etPrice.getText().toString().trim());
            intent.putExtra("tipoPropiedad", actvPropertyType.getText().toString().trim());
            
            // Obtenemos la ubicación completa
            String departamento = actvDepartamento.getText().toString().trim();
            String municipio = actvMunicipio.getText().toString().trim();
            String direccion = etAddress.getText().toString().trim();
            String ubicacionCompleta = municipio + ", " + departamento;
            intent.putExtra("ubicacion", ubicacionCompleta);
            intent.putExtra("direccionCompleta", direccion + ", " + ubicacionCompleta);
            
            // Otros detalles importantes
            intent.putExtra("area", etArea.getText().toString().trim());
            
            // Validación de campos numéricos con valores por defecto
            int dormitorios = 0;
            try {
                dormitorios = Integer.parseInt(etBedrooms.getText().toString().trim());
            } catch (NumberFormatException e) {
                // Usar valor por defecto
                Log.w(TAG, "Error al parsear dormitorios, usando valor por defecto");
            }
            intent.putExtra("dormitorios", dormitorios);
            
            int banos = 0;
            try {
                banos = Integer.parseInt(etBathrooms.getText().toString().trim());
            } catch (NumberFormatException e) {
                // Usar valor por defecto
                Log.w(TAG, "Error al parsear baños, usando valor por defecto");
            }
            intent.putExtra("banos", banos);
            
            // Estado (por defecto "Disponible")
            intent.putExtra("estado", "Disponible");
            
            // Datos del vendedor
            intent.putExtra("nombreVendedor", etContactName.getText().toString().trim());
            intent.putExtra("empresaVendedor", etCompany.getText().toString().trim());
            intent.putExtra("telefono", etPhoneNumber.getText().toString().trim());
            intent.putExtra("email", etEmail.getText().toString().trim());
            
            // Log para confirmar que los datos están siendo enviados correctamente
            Log.d(TAG, "Enviando datos a DetallePropiedad: " +
                  "\nTítulo: " + etTitle.getText().toString().trim() +
                  "\nTipo: " + actvPropertyType.getText().toString().trim() +
                  "\nDormitorios: " + dormitorios +
                  "\nBaños: " + banos +
                  "\nÁrea: " + etArea.getText().toString().trim());
            
            // Iniciar la actividad
            startActivity(intent);
            Toast.makeText(requireContext(), "Propiedad publicada con éxito", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error al enviar datos: " + e.getMessage());
            Toast.makeText(requireContext(), "Error al publicar propiedad: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
