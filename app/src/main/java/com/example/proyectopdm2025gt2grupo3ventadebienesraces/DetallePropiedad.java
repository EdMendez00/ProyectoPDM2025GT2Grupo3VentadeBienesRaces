package com.example.proyectopdm2025gt2grupo3ventadebienesraces;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class DetallePropiedad extends AppCompatActivity {

    private static final String TAG = "DetallePropiedad";

    // Variables para las vistas
    private ImageView imgPropiedad;
    private TextView tvTitulo, tvUbicacion, tvPrecio, tvDescripcion;
    private TextView tvDormitorios, tvBanos, tvArea;
    private TextView tvTipoPropiedad, tvSuperficie, tvDormitoriosDetalle, tvBanosDetalle;
    private TextView tvUbicacionDetalle, tvEstadoDetalle, tvEstado;
    private ImageButton btnBack, btnPrevious, btnNext;
    private LinearLayout btnLlamar, btnAgendarVisita;
    private TextView tvNombreVendedor, tvEmpresaVendedor, tvTelefono, tvEmailVendedor;
    private TabLayout tabLayout;
    private LinearLayout layoutDetalles, layoutCaracteristicas, layoutContacto;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_propiedad);

        // Inicializar vistas
        inicializarVistas();
        
        // Configurar botón de retroceso
        btnBack.setOnClickListener(v -> finish());
        
        // Recibir datos pasados como intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cargarDatosDesdeExtras(extras);
        } else {
            Toast.makeText(this, "No se recibieron datos de la propiedad", Toast.LENGTH_SHORT).show();
        }
        
        // Configurar tabs
        configurarTabs();
    }

    private void inicializarVistas() {
        try {
            // Referencias a todas las vistas necesarias
            imgPropiedad = findViewById(R.id.imgPropiedad);
            tvTitulo = findViewById(R.id.tvTitulo);
            tvUbicacion = findViewById(R.id.tvUbicacion);
            tvPrecio = findViewById(R.id.tvPrecio);
            tvDescripcion = findViewById(R.id.tvDescripcion);
            tvDormitorios = findViewById(R.id.tvDormitorios);
            tvBanos = findViewById(R.id.tvBanos);
            tvArea = findViewById(R.id.tvArea);
            
            // Detalles adicionales
            tvTipoPropiedad = findViewById(R.id.tvTipoPropiedad);
            tvSuperficie = findViewById(R.id.tvSuperficie);
            tvDormitoriosDetalle = findViewById(R.id.tvDormitoriosDetalle);
            tvBanosDetalle = findViewById(R.id.tvBanosDetalle);
            tvUbicacionDetalle = findViewById(R.id.tvUbicacionDetalle);
            tvEstadoDetalle = findViewById(R.id.tvEstadoDetalle);
            tvEstado = findViewById(R.id.tvEstado);
            
            // Botones
            btnBack = findViewById(R.id.btnBack);
            btnPrevious = findViewById(R.id.btnPrevious);
            btnNext = findViewById(R.id.btnNext);
            
            // Layouts de contacto
            btnLlamar = findViewById(R.id.btnLlamar);
            btnAgendarVisita = findViewById(R.id.btnAgendarVisita);
            tvNombreVendedor = findViewById(R.id.tvNombreVendedor);
            tvEmpresaVendedor = findViewById(R.id.tvEmpresaVendedor);
            tvTelefono = findViewById(R.id.tvTelefono);
            tvEmailVendedor = findViewById(R.id.tvEmailVendedor);

            // Tabs y layouts
            tabLayout = findViewById(R.id.tabLayout);
            layoutDetalles = findViewById(R.id.layoutDetalles);
            layoutCaracteristicas = findViewById(R.id.layoutCaracteristicas);
            layoutContacto = findViewById(R.id.layoutContacto);
            
            // Bottom navigation
            bottomNavigation = findViewById(R.id.bottom_navigation);
        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar vistas: " + e.getMessage());
            Toast.makeText(this, "Error al inicializar la interfaz", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarDatosDesdeExtras(Bundle extras) {
        try {
            // Datos básicos de la propiedad
            String titulo = extras.getString("titulo", "No disponible");
            String descripcion = extras.getString("descripcion", "No disponible");
            String precio = extras.getString("precio", "0");
            String ubicacion = extras.getString("ubicacion", "No disponible");
            String tipoPropiedad = extras.getString("tipoPropiedad", "No disponible");
            String area = extras.getString("area", "0");
            int dormitorios = extras.getInt("dormitorios", 0);
            int banos = extras.getInt("banos", 0);
            String estado = extras.getString("estado", "Disponible");
            
            // Datos del vendedor (obtenidos desde el formulario de "agregar nueva publicación")
            String nombreVendedor = extras.getString("nombreVendedor", "No disponible");
            String empresaVendedor = extras.getString("empresaVendedor", "No disponible");
            String telefono = extras.getString("telefono", "No disponible");
            String email = extras.getString("email", "No disponible");

            // Log para debugging
            Log.d(TAG, "Datos recibidos: " +
                  "\nTítulo: " + titulo +
                  "\nTipo: " + tipoPropiedad +
                  "\nDormitorios: " + dormitorios +
                  "\nBaños: " + banos +
                  "\nÁrea: " + area +
                  "\nUbicación: " + ubicacion);
            
            // Log específico para los datos de contacto
            Log.d(TAG, "Datos de contacto recibidos desde formulario de publicación: " +
                  "\nNombre: " + nombreVendedor +
                  "\nEmpresa: " + empresaVendedor +
                  "\nTeléfono: " + telefono +
                  "\nEmail: " + email);

            // Establecer datos en las vistas
            tvTitulo.setText(titulo);
            tvDescripcion.setText(descripcion);
            tvPrecio.setText("$" + precio);
            tvUbicacion.setText(ubicacion);
            
            // Características principales
            tvDormitorios.setText(String.valueOf(dormitorios));
            tvBanos.setText(String.valueOf(banos));
            tvArea.setText(area + " m²");
            tvEstado.setText(estado);
            
            // Establecer detalles adicionales
            tvTipoPropiedad.setText(tipoPropiedad);
            tvSuperficie.setText(area + " m²");
            tvDormitoriosDetalle.setText(String.valueOf(dormitorios));
            tvBanosDetalle.setText(String.valueOf(banos));
            tvUbicacionDetalle.setText(ubicacion);
            tvEstadoDetalle.setText(estado);
            
            // Establecer datos del vendedor
            tvNombreVendedor.setText(nombreVendedor);
            tvEmpresaVendedor.setText(empresaVendedor);
            tvTelefono.setText(telefono);
            tvEmailVendedor.setText(email);

        } catch (Exception e) {
            Log.e(TAG, "Error al cargar datos: " + e.getMessage());
            Toast.makeText(this, "Error al cargar los datos de la propiedad", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mostrarLayoutSeleccionado(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No se requiere acción
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No se requiere acción
            }
        });
        
        // Mostrar la primera pestaña por defecto
        mostrarLayoutSeleccionado(0);
    }
    
    private void mostrarLayoutSeleccionado(int position) {
        // Ocultar todos los layouts primero
        layoutDetalles.setVisibility(View.GONE);
        layoutCaracteristicas.setVisibility(View.GONE);
        layoutContacto.setVisibility(View.GONE);
        
        // Mostrar el layout seleccionado
        switch (position) {
            case 0:
                layoutDetalles.setVisibility(View.VISIBLE);
                break;
            case 1:
                layoutCaracteristicas.setVisibility(View.VISIBLE);
                break;
            case 2:
                layoutContacto.setVisibility(View.VISIBLE);
                break;
        }
    }
}
