-- =================================================================
-- DATOS DE PRUEBA - SISTEMA VENTA DE BIENES RAÍCES
-- Universidad de El Salvador - PDM115
-- Grupo Teórico 02 - Grupo de Proyecto 03
-- =================================================================

USE ventabienesraices_db;

-- Deshabilitar verificaciones de claves foráneas temporalmente
SET FOREIGN_KEY_CHECKS = 0;

-- =================================================================
-- INSERTAR USUARIOS DE PRUEBA
-- =================================================================

-- Usuario Administrador
INSERT INTO usuarios (
    id, nombre, apellido, correo, telefono, rol, firebase_uid, 
    foto_perfil, direccion, genero, verificado, activo
) VALUES (
    'admin001', 'María', 'Administradora', 'admin@ventabienes.com', 
    '+503 7123-4567', 'admin', 'firebase_admin_001',
    'https://example.com/fotos/admin.jpg', 
    'Colonia Médica, San Salvador', 'femenino', TRUE, TRUE
);

-- Usuarios Vendedores
INSERT INTO usuarios (
    id, nombre, apellido, correo, telefono, rol, firebase_uid,
    foto_perfil, direccion, genero, verificado, activo
) VALUES 
(
    'vendor001', 'Carlos', 'Vendedor López', 'carlos.vendedor@gmail.com',
    '+503 7234-5678', 'vendedor', 'firebase_vendor_001',
    'https://example.com/fotos/carlos.jpg',
    'Colonia Escalón, San Salvador', 'masculino', TRUE, TRUE
),
(
    'vendor002', 'Ana', 'Martínez Sales', 'ana.martinez@inmobiliaria.com',
    '+503 7345-6789', 'vendedor', 'firebase_vendor_002',
    'https://example.com/fotos/ana.jpg',
    'Colonia San Benito, San Salvador', 'femenino', TRUE, TRUE
),
(
    'vendor003', 'Roberto', 'Inmobiliaria Pro', 'roberto@propiedades.sv',
    '+503 7456-7890', 'vendedor', 'firebase_vendor_003',
    'https://example.com/fotos/roberto.jpg',
    'Santa Tecla, La Libertad', 'masculino', TRUE, TRUE
);

-- Usuarios Clientes
INSERT INTO usuarios (
    id, nombre, apellido, correo, telefono, rol, firebase_uid,
    foto_perfil, direccion, genero, verificado, activo
) VALUES 
(
    'client001', 'Ana', 'Cliente Gómez', 'ana.cliente@gmail.com',
    '+503 7345-6789', 'cliente', 'firebase_client_001',
    'https://example.com/fotos/ana_cliente.jpg',
    'Mejicanos, San Salvador', 'femenino', TRUE, TRUE
),
(
    'client002', 'Luis', 'Comprador Pérez', 'luis.comprador@yahoo.com',
    '+503 7567-8901', 'cliente', 'firebase_client_002',
    'https://example.com/fotos/luis.jpg',
    'Antiguo Cuscatlán, La Libertad', 'masculino', TRUE, TRUE
),
(
    'client003', 'Carmen', 'Familia Rodríguez', 'carmen.familia@outlook.com',
    '+503 7678-9012', 'cliente', 'firebase_client_003',
    'https://example.com/fotos/carmen.jpg',
    'Soyapango, San Salvador', 'femenino', TRUE, TRUE
),
(
    'client004', 'Miguel', 'Joven Profesional', 'miguel.profesional@gmail.com',
    '+503 7789-0123', 'cliente', 'firebase_client_004',
    'https://example.com/fotos/miguel.jpg',
    'San Miguel, San Miguel', 'masculino', TRUE, TRUE
);

-- =================================================================
-- INSERTAR PROPIEDADES DE PRUEBA
-- =================================================================

-- Casa Residencial en Colonia Escalón
INSERT INTO propiedades (
    titulo, descripcion, precio, direccion, latitud, longitud,
    largo, ancho, area, tipo_propiedad, dormitorios, banos, cocheras,
    pisos, caracteristicas, usuario_id, estado, medio_contacto,
    destacada, precio_negociable, amueblada, mascotas_permitidas,
    servicios_incluidos, condicion_propiedad, ano_construccion
) VALUES (
    'Casa Residencial en Colonia Escalón',
    'Hermosa casa de dos plantas en exclusiva colonia Escalón. Cuenta con amplio jardín, sala, comedor, cocina equipada, 4 dormitorios y 3 baños completos. Perfecta para familia grande. Zona muy segura con vigilancia 24/7.',
    185000.00, 'Colonia Escalón, Calle Los Bambúes #123, San Salvador',
    13.7068, -89.2257, 15.50, 12.00, 186.00, 'casa', 4, 3, 2, 2,
    'Jardín amplio,Cochera para 2 vehículos,Cocina equipada,Aire acondicionado,Seguridad 24/7,Área de lavandería,Terraza,Bodega',
    'vendor001', 'DISPONIBLE', 'WhatsApp: +503 7234-5678, Email: carlos.vendedor@gmail.com',
    TRUE, TRUE, FALSE, TRUE,
    'Agua,Luz,Internet,Cable,Seguridad', 'usada', 2018
);

-- Apartamento Moderno en Torre Ejecutiva
INSERT INTO propiedades (
    titulo, descripcion, precio, direccion, latitud, longitud,
    largo, ancho, area, tipo_propiedad, dormitorios, banos, cocheras,
    pisos, caracteristicas, usuario_id, estado, medio_contacto,
    destacada, precio_negociable, amueblada, mascotas_permitidas,
    servicios_incluidos, condicion_propiedad, ano_construccion
) VALUES (
    'Apartamento Moderno en Torre Ejecutiva',
    'Moderno apartamento en torre ejecutiva con vista panorámica de la ciudad. Ubicado en zona céntrica con fácil acceso a centros comerciales y transporte público. Ideal para profesionales o pareja joven.',
    95000.00, 'Centro Histórico, Torre Millennium, Piso 12, San Salvador',
    13.6969, -89.1914, 10.00, 8.50, 85.00, 'apartamento', 2, 2, 1, 1,
    'Vista panorámica,Piscina común,Gimnasio,Parqueo incluido,Portón eléctrico,Ascensor,Balcón,Sala de estar',
    'vendor001', 'DISPONIBLE', 'Teléfono: +503 7234-5678, WhatsApp disponible',
    TRUE, TRUE, TRUE, FALSE,
    'Agua,Luz,Internet,Mantenimiento,Seguridad', 'nueva', 2022
);

-- Local Comercial en Metrocentro
INSERT INTO propiedades (
    titulo, descripcion, precio, direccion, latitud, longitud,
    largo, ancho, area, tipo_propiedad, dormitorios, banos, cocheras,
    pisos, caracteristicas, usuario_id, estado, medio_contacto,
    destacada, precio_negociable, amueblada, mascotas_permitidas,
    servicios_incluidos, condicion_propiedad, ano_construccion
) VALUES (
    'Local Comercial en Metrocentro',
    'Excelente local comercial en centro comercial Metrocentro. Ideal para negocio de ropa, restaurante o oficina. Alta afluencia de personas, ubicación estratégica. Incluye bodega pequeña.',
    125000.00, 'Metrocentro, Local 234, Segunda Planta, San Salvador',
    13.7020, -89.2272, 8.00, 6.00, 48.00, 'local_comercial', 0, 1, 0, 1,
    'Alta afluencia,Aire acondicionado central,Vitrina amplia,Seguridad del centro comercial,Parqueo abundante,Bodega incluida',
    'vendor002', 'DISPONIBLE', 'Email: ana.martinez@inmobiliaria.com, Teléfono: +503 7345-6789',
    FALSE, TRUE, FALSE, FALSE,
    'Luz,Agua,Aire acondicionado,Seguridad', 'usada', 2015
);

-- Casa Económica en Mejicanos
INSERT INTO propiedades (
    titulo, descripcion, precio, direccion, latitud, longitud,
    largo, ancho, area, tipo_propiedad, dormitorios, banos, cocheras,
    pisos, caracteristicas, usuario_id, estado, medio_contacto,
    destacada, precio_negociable, amueblada, mascotas_permitidas,
    servicios_incluidos, condicion_propiedad, ano_construccion
) VALUES (
    'Casa Familiar en Mejicanos - Precio Accesible',
    'Casa familiar en excelente ubicación en Mejicanos. Perfecta para primera vivienda. Cuenta con sala, comedor, cocina, 3 dormitorios y 2 baños. Patio trasero amplio. Cerca de escuelas y transporte público.',
    45000.00, 'Mejicanos, Colonia Zacamil, Pasaje Los Laureles #45',
    13.7200, -89.2100, 12.00, 8.00, 96.00, 'casa', 3, 2, 0, 1,
    'Patio amplio,Cerca de transporte,Zona residencial,Cocina equipada,Cerca de escuelas,Acceso fácil',
    'vendor002', 'DISPONIBLE', 'WhatsApp: +503 7345-6789',
    FALSE, TRUE, FALSE, TRUE,
    'Agua,Luz', 'usada', 2010
);

-- Apartamento de Lujo en Santa Tecla
INSERT INTO propiedades (
    titulo, descripcion, precio, direccion, latitud, longitud,
    largo, ancho, area, tipo_propiedad, dormitorios, banos, cocheras,
    pisos, caracteristicas, usuario_id, estado, medio_contacto,
    destacada, precio_negociable, amueblada, mascotas_permitidas,
    servicios_incluidos, condicion_propiedad, ano_construccion
) VALUES (
    'Apartamento de Lujo con Vista al Volcán',
    'Espectacular apartamento de lujo en Santa Tecla con vista al volcán de San Salvador. Acabados de primera calidad, cocina gourmet, terraza privada. Exclusivo complejo residencial con todas las amenidades.',
    220000.00, 'Santa Tecla, Residencial Las Colinas, Torre A, Piso 8',
    13.6733, -89.2794, 14.00, 10.00, 140.00, 'apartamento', 3, 2, 2, 1,
    'Vista al volcán,Terraza privada,Cocina gourmet,Pisos de mármol,Jacuzzi,Walk-in closet,Chimenea,Club house',
    'vendor003', 'DISPONIBLE', 'Roberto Inmobiliaria: +503 7456-7890, roberto@propiedades.sv',
    TRUE, FALSE, TRUE, TRUE,
    'Agua,Luz,Internet,Cable,Mantenimiento,Seguridad,Jardinería', 'nueva', 2023
);

-- Terreno para Construcción
INSERT INTO propiedades (
    titulo, descripcion, precio, direccion, latitud, longitud,
    largo, ancho, area, tipo_propiedad, dormitorios, banos, cocheras,
    pisos, caracteristicas, usuario_id, estado, medio_contacto,
    destacada, precio_negociable, amueblada, mascotas_permitidas,
    servicios_incluidos, condicion_propiedad, ano_construccion
) VALUES (
    'Terreno Residencial para Construcción',
    'Excelente terreno en zona residencial de rápido crecimiento. Ideal para construcción de casa familiar. Cuenta con todos los servicios disponibles. Documentación en regla. Zona tranquila y segura.',
    38000.00, 'Antiguo Cuscatlán, Residencial El Roble, Lote #15',
    13.6644, -89.2520, 20.00, 15.00, 300.00, 'terreno', 0, 0, 0, 0,
    'Servicios disponibles,Documentación en regla,Zona residencial,Acceso pavimentado,Cerca de centros comerciales',
    'vendor003', 'DISPONIBLE', 'Contacto directo: +503 7456-7890',
    FALSE, TRUE, FALSE, FALSE,
    'Acceso a agua,Acceso a luz', 'nueva', 2024
);

-- Oficina en Zona Rosa
INSERT INTO propiedades (
    titulo, descripcion, precio, direccion, latitud, longitud,
    largo, ancho, area, tipo_propiedad, dormitorios, banos, cocheras,
    pisos, caracteristicas, usuario_id, estado, medio_contacto,
    destacada, precio_negociable, amueblada, mascotas_permitidas,
    servicios_incluidos, condicion_propiedad, ano_construccion
) VALUES (
    'Oficina Ejecutiva en Zona Rosa',
    'Moderna oficina en el corazón de la Zona Rosa. Perfecta para empresas, consultorios o despachos profesionales. Edificio corporativo con todas las amenidades. Excelente presencia comercial.',
    78000.00, 'Zona Rosa, Edificio Corporate Center, Oficina 405',
    13.7020, -89.2350, 8.00, 6.00, 48.00, 'oficina', 0, 2, 1, 1,
    'Zona empresarial,Recepción incluida,Sala de juntas,Internet de alta velocidad,Aire acondicionado,Ascensor',
    'vendor001', 'DISPONIBLE', 'Carlos Vendedor: carlos.vendedor@gmail.com, +503 7234-5678',
    FALSE, TRUE, TRUE, FALSE,
    'Agua,Luz,Internet,Aire acondicionado,Mantenimiento,Seguridad', 'remodelada', 2020
);

-- =================================================================
-- INSERTAR IMÁGENES DE PROPIEDADES
-- =================================================================

-- Imágenes para Casa en Escalón
INSERT INTO imagenes_propiedad (
    propiedad_id, url, descripcion, es_principal, orden_visualizacion, 
    formato_imagen, subida_por
) VALUES 
(1, 'https://example.com/propiedades/casa_escalon_frente.jpg', 'Vista frontal de la casa', TRUE, 1, 'jpg', 'vendor001'),
(1, 'https://example.com/propiedades/casa_escalon_sala.jpg', 'Sala principal', FALSE, 2, 'jpg', 'vendor001'),
(1, 'https://example.com/propiedades/casa_escalon_cocina.jpg', 'Cocina equipada', FALSE, 3, 'jpg', 'vendor001'),
(1, 'https://example.com/propiedades/casa_escalon_jardin.jpg', 'Jardín trasero', FALSE, 4, 'jpg', 'vendor001'),
(1, 'https://example.com/propiedades/casa_escalon_dormitorio.jpg', 'Dormitorio principal', FALSE, 5, 'jpg', 'vendor001');

-- Imágenes para Apartamento Torre Ejecutiva
INSERT INTO imagenes_propiedad (
    propiedad_id, url, descripcion, es_principal, orden_visualizacion,
    formato_imagen, subida_por
) VALUES 
(2, 'https://example.com/propiedades/apt_ejecutivo_vista.jpg', 'Vista panorámica desde el balcón', TRUE, 1, 'jpg', 'vendor001'),
(2, 'https://example.com/propiedades/apt_ejecutivo_sala.jpg', 'Sala moderna', FALSE, 2, 'jpg', 'vendor001'),
(2, 'https://example.com/propiedades/apt_ejecutivo_cocina.jpg', 'Cocina integral', FALSE, 3, 'jpg', 'vendor001'),
(2, 'https://example.com/propiedades/apt_ejecutivo_dormitorio.jpg', 'Dormitorio principal', FALSE, 4, 'jpg', 'vendor001');

-- Imágenes para Local Comercial
INSERT INTO imagenes_propiedad (
    propiedad_id, url, descripcion, es_principal, orden_visualizacion,
    formato_imagen, subida_por
) VALUES 
(3, 'https://example.com/propiedades/local_metrocentro_frente.jpg', 'Frente del local', TRUE, 1, 'jpg', 'vendor002'),
(3, 'https://example.com/propiedades/local_metrocentro_interior.jpg', 'Interior amplio', FALSE, 2, 'jpg', 'vendor002'),
(3, 'https://example.com/propiedades/local_metrocentro_vitrina.jpg', 'Vitrina hacia el pasillo', FALSE, 3, 'jpg', 'vendor002');

-- =================================================================
-- INSERTAR FAVORITOS DE PRUEBA
-- =================================================================

INSERT INTO favoritos (usuario_id, propiedad_id, notas_personales) VALUES 
('client001', 1, 'Me encanta el jardín y la ubicación'),
('client001', 2, 'Perfecto para mi trabajo en el centro'),
('client002', 1, 'Ideal para mi familia'),
('client002', 5, 'Me gusta el lujo y la vista'),
('client003', 4, 'Precio accesible para primera casa'),
('client004', 2, 'Perfecto para joven profesional'),
('client004', 7, 'Buena opción para oficina');

-- =================================================================
-- INSERTAR VISITAS PROGRAMADAS
-- =================================================================

INSERT INTO visitas (
    propiedad_id, cliente_id, vendedor_id, fecha_visita, hora_visita,
    estado, notas_cliente, telefono_contacto, email_contacto
) VALUES 
(1, 'client001', 'vendor001', '2025-01-20', '15:00:00', 'programada', 
 'Interesada en conocer el jardín y la cocina', '+503 7345-6789', 'ana.cliente@gmail.com'),
(1, 'client002', 'vendor001', '2025-01-22', '10:30:00', 'confirmada',
 'Quiere ver con la familia completa', '+503 7567-8901', 'luis.comprador@yahoo.com'),
(2, 'client001', 'vendor001', '2025-01-18', '16:00:00', 'realizada',
 'Muy interesada, solicita información de financiamiento', '+503 7345-6789', 'ana.cliente@gmail.com'),
(4, 'client003', 'vendor002', '2025-01-25', '11:00:00', 'programada',
 'Primera casa, necesita asesoría completa', '+503 7678-9012', 'carmen.familia@outlook.com'),
(5, 'client002', 'vendor003', '2025-01-21', '14:00:00', 'confirmada',
 'Interesado en apartamento de lujo', '+503 7567-8901', 'luis.comprador@yahoo.com'),
(7, 'client004', 'vendor001', '2025-01-19', '09:00:00', 'realizada',
 'Para oficina de consultoría', '+503 7789-0123', 'miguel.profesional@gmail.com');

-- =================================================================
-- INSERTAR BÚSQUEDAS GUARDADAS
-- =================================================================

INSERT INTO busquedas_guardadas (usuario_id, nombre_busqueda, criterios_busqueda) VALUES 
('client001', 'Casas en Escalón', 
 '{"tipo_propiedad": "casa", "zona": "Escalón", "precio_max": 200000, "dormitorios_min": 3}'),
('client002', 'Apartamentos modernos',
 '{"tipo_propiedad": "apartamento", "año_min": 2020, "precio_max": 150000, "amueblado": true}'),
('client003', 'Primera vivienda económica',
 '{"precio_max": 60000, "dormitorios_min": 2, "zona": ["Mejicanos", "Soyapango"]}'),
('client004', 'Oficinas Zona Rosa',
 '{"tipo_propiedad": "oficina", "zona": "Zona Rosa", "precio_max": 100000}');

-- =================================================================
-- INSERTAR MENSAJES DE PRUEBA
-- =================================================================

INSERT INTO mensajes (
    remitente_id, destinatario_id, propiedad_id, asunto, mensaje, tipo_mensaje
) VALUES 
('client001', 'vendor001', 1, 'Consulta sobre casa en Escalón',
 'Hola, estoy muy interesada en la casa de Colonia Escalón. Me gustaría saber si es posible negociar el precio y si está disponible para visita este fin de semana.', 'consulta'),
('vendor001', 'client001', 1, 'Re: Consulta sobre casa en Escalón',
 'Hola Ana, gracias por tu interés. Sí podemos programar una visita para este sábado. En cuanto al precio, hay un pequeño margen de negociación. ¿Te parece bien a las 3 PM?', 'consulta'),
('client002', 'vendor003', 5, 'Información sobre financiamiento',
 'Buenos días, me interesa el apartamento de lujo en Santa Tecla. ¿Manejan opciones de financiamiento? ¿Cuál sería el enganche mínimo?', 'consulta'),
('client004', 'vendor001', 7, 'Oferta por oficina',
 'Estimado Carlos, después de ver la oficina me interesa mucho. ¿Estarían dispuestos a considerar una oferta de $75,000?', 'oferta');

-- =================================================================
-- INSERTAR CONFIGURACIONES DE USUARIO
-- =================================================================

INSERT INTO configuraciones_usuario (
    usuario_id, notificaciones_email, notificaciones_push, 
    notificaciones_nuevas_propiedades, radio_busqueda_km,
    configuraciones_privacidad, tema_aplicacion
) VALUES 
('client001', TRUE, TRUE, TRUE, 25, 
 '{"mostrar_telefono": false, "mostrar_email": true, "permitir_mensajes": true}', 'claro'),
('client002', TRUE, FALSE, TRUE, 50,
 '{"mostrar_telefono": true, "mostrar_email": true, "permitir_mensajes": true}', 'oscuro'),
('vendor001', TRUE, TRUE, TRUE, 100,
 '{"mostrar_telefono": true, "mostrar_email": true, "mostrar_estadisticas": true}', 'claro'),
('vendor002', TRUE, TRUE, FALSE, 75,
 '{"mostrar_telefono": true, "mostrar_email": true, "mostrar_estadisticas": true}', 'automatico');

-- =================================================================
-- INSERTAR ESTADÍSTICAS DE PROPIEDADES
-- =================================================================

INSERT INTO estadisticas_propiedades (
    propiedad_id, fecha, visualizaciones, clicks_telefono, 
    clicks_whatsapp, favoritos_agregados, visitas_programadas
) VALUES 
(1, '2025-01-15', 45, 8, 12, 3, 2),
(1, '2025-01-16', 38, 5, 9, 1, 1),
(2, '2025-01-15', 67, 12, 15, 2, 1),
(2, '2025-01-16', 52, 8, 11, 1, 0),
(3, '2025-01-15', 23, 3, 2, 0, 0),
(4, '2025-01-15', 34, 6, 8, 1, 1),
(5, '2025-01-15', 89, 15, 18, 1, 1),
(6, '2025-01-15', 12, 2, 1, 0, 0),
(7, '2025-01-15', 28, 4, 3, 1, 1);

-- =================================================================
-- ACTUALIZAR CONTADORES EN PROPIEDADES
-- =================================================================

UPDATE propiedades SET vistas = 83 WHERE id = 1;
UPDATE propiedades SET vistas = 119 WHERE id = 2;
UPDATE propiedades SET vistas = 23 WHERE id = 3;
UPDATE propiedades SET vistas = 34 WHERE id = 4;
UPDATE propiedades SET vistas = 89 WHERE id = 5;
UPDATE propiedades SET vistas = 12 WHERE id = 6;
UPDATE propiedades SET vistas = 28 WHERE id = 7;

-- Actualizar imagen principal en propiedades
UPDATE propiedades SET imagen_principal = 'https://example.com/propiedades/casa_escalon_frente.jpg' WHERE id = 1;
UPDATE propiedades SET imagen_principal = 'https://example.com/propiedades/apt_ejecutivo_vista.jpg' WHERE id = 2;
UPDATE propiedades SET imagen_principal = 'https://example.com/propiedades/local_metrocentro_frente.jpg' WHERE id = 3;

-- =================================================================
-- INSERTAR LOGS DE ACTIVIDAD RECIENTES
-- =================================================================

INSERT INTO logs_actividad (
    usuario_id, accion, tabla_afectada, registro_id, 
    datos_nuevos, ip_address, fecha_accion, exito
) VALUES 
('vendor001', 'INSERT', 'propiedades', '1', 
 '{"titulo": "Casa Residencial en Colonia Escalón", "precio": 185000}', 
 '192.168.1.100', '2025-01-15 10:30:00', TRUE),
('client001', 'INSERT', 'favoritos', '1',
 '{"propiedad_id": 1, "usuario_id": "client001"}',
 '192.168.1.101', '2025-01-15 14:20:00', TRUE),
('client001', 'INSERT', 'visitas', '1',
 '{"propiedad_id": 1, "fecha_visita": "2025-01-20"}',
 '192.168.1.101', '2025-01-16 09:15:00', TRUE);

-- Rehabilitar verificaciones de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;

-- =================================================================
-- RESUMEN DE DATOS INSERTADOS
-- =================================================================

/*
DATOS DE PRUEBA INSERTADOS:

USUARIOS:
- 1 Administrador: admin@ventabienes.com
- 3 Vendedores: carlos.vendedor@gmail.com, ana.martinez@inmobiliaria.com, roberto@propiedades.sv
- 4 Clientes: ana.cliente@gmail.com, luis.comprador@yahoo.com, carmen.familia@outlook.com, miguel.profesional@gmail.com

PROPIEDADES:
- 7 propiedades variadas (casas, apartamentos, oficina, terreno, local comercial)
- Precios desde $38,000 hasta $220,000
- Diferentes ubicaciones en El Salvador

INTERACCIONES:
- 7 favoritos
- 6 visitas programadas/realizadas
- 4 búsquedas guardadas
- 4 mensajes entre usuarios
- Estadísticas de visualización

CREDENCIALES DE PRUEBA:
- Email: admin@ventabienes.com | Password: Admin123456
- Email: carlos.vendedor@gmail.com | Password: Vendor123456
- Email: ana.cliente@gmail.com | Password: Client123456

NOTA: Las contraseñas deben configurarse en Firebase Authentication
Las URLs de imágenes son ejemplos y deben reemplazarse por URLs reales
*/
