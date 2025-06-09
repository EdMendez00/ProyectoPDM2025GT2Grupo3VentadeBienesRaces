-- =================================================================
-- SETUP DE BASE DE DATOS ROOM (SQLite) - APLICACIÓN ANDROID
-- Sistema de Venta de Bienes Raíces
-- Universidad de El Salvador - PDM115
-- Grupo Teórico 02 - Grupo de Proyecto 03
-- =================================================================

-- Este archivo contiene los scripts SQL para crear y poblar la base de datos 
-- Room (SQLite) que utiliza la aplicación Android de forma local.

-- =================================================================
-- CREACIÓN DE TABLAS
-- =================================================================

-- Tabla: usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
    `id` TEXT NOT NULL, 
    `nombre` TEXT NOT NULL, 
    `correo` TEXT NOT NULL, 
    `telefono` TEXT NOT NULL, 
    `rol` TEXT NOT NULL, 
    `fechaRegistro` INTEGER NOT NULL, 
    PRIMARY KEY(`id`)
);

-- Tabla: propiedades
CREATE TABLE IF NOT EXISTS `propiedades` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
    `titulo` TEXT NOT NULL, 
    `descripcion` TEXT NOT NULL, 
    `precio` REAL NOT NULL, 
    `direccion` TEXT NOT NULL, 
    `latitud` REAL NOT NULL, 
    `longitud` REAL NOT NULL, 
    `largo` REAL NOT NULL, 
    `ancho` REAL NOT NULL, 
    `area` REAL NOT NULL, 
    `tipoPropiedad` TEXT NOT NULL, 
    `dormitorios` INTEGER NOT NULL, 
    `banos` INTEGER NOT NULL, 
    `caracteristicas` TEXT NOT NULL, 
    `vendedorId` TEXT NOT NULL, 
    `fechaPublicacion` INTEGER NOT NULL, 
    `estado` TEXT NOT NULL, 
    `medioContacto` TEXT NOT NULL,
    FOREIGN KEY(`vendedorId`) REFERENCES `usuarios`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

-- Tabla: imagenes_propiedad
CREATE TABLE IF NOT EXISTS `imagenes_propiedad` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
    `propiedadId` INTEGER NOT NULL, 
    `rutaImagen` TEXT NOT NULL, 
    FOREIGN KEY(`propiedadId`) REFERENCES `propiedades`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

-- Tabla: visitas
CREATE TABLE IF NOT EXISTS `visitas` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
    `propiedadId` INTEGER NOT NULL, 
    `clienteId` TEXT NOT NULL, 
    `vendedorId` TEXT NOT NULL, 
    `fecha` INTEGER NOT NULL, 
    `hora` TEXT NOT NULL, 
    `estado` TEXT NOT NULL, 
    `comentarios` TEXT NOT NULL, 
    FOREIGN KEY(`propiedadId`) REFERENCES `propiedades`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
    FOREIGN KEY(`clienteId`) REFERENCES `usuarios`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
    FOREIGN KEY(`vendedorId`) REFERENCES `usuarios`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

-- =================================================================
-- CREACIÓN DE ÍNDICES PARA OPTIMIZACIÓN
-- =================================================================

CREATE INDEX IF NOT EXISTS `index_propiedades_vendedorId` ON `propiedades` (`vendedorId`);
CREATE INDEX IF NOT EXISTS `index_propiedades_tipoPropiedad` ON `propiedades` (`tipoPropiedad`);
CREATE INDEX IF NOT EXISTS `index_propiedades_estado` ON `propiedades` (`estado`);
CREATE INDEX IF NOT EXISTS `index_propiedades_precio` ON `propiedades` (`precio`);

CREATE INDEX IF NOT EXISTS `index_imagenes_propiedad_propiedadId` ON `imagenes_propiedad` (`propiedadId`);

CREATE INDEX IF NOT EXISTS `index_visitas_propiedadId` ON `visitas` (`propiedadId`);
CREATE INDEX IF NOT EXISTS `index_visitas_clienteId` ON `visitas` (`clienteId`);
CREATE INDEX IF NOT EXISTS `index_visitas_vendedorId` ON `visitas` (`vendedorId`);
CREATE INDEX IF NOT EXISTS `index_visitas_fecha` ON `visitas` (`fecha`);

-- =================================================================
-- INSERCIÓN DE DATOS DE PRUEBA
-- =================================================================

-- Insertar usuarios de prueba
INSERT OR REPLACE INTO usuarios (id, nombre, correo, telefono, rol, fechaRegistro) VALUES 
('admin001', 'Administrador General', 'admin@realestate.com', '+503 7000-0000', 'admin', 1704067200000),
('cargueta01', 'Carlos Argueta', 'carlos.argueta@vendedor.com', '+503 7123-4567', 'vendedor', 1704067200000),
('samuel001', 'Samuel Alas', 'samuel.alas@vendedor.com', '+503 7234-5678', 'vendedor', 1704067200000),
('pendragon503', 'Pendragon Propiedades', 'pendragon503@vendedor.com', '+503 7345-6789', 'vendedor', 1704067200000),
('eduardo001', 'Eduardo Méndez', 'eduardo.mendez@tech.com', '+503 7456-7890', 'vendedor', 1704067200000),
('cliente001', 'Ana García', 'ana.garcia@email.com', '+503 6999-8888', 'cliente', 1704067200000),
('cliente002', 'María Fernández', 'maria.fernandez@email.com', '+503 6111-2222', 'cliente', 1704067200000),
('cliente003', 'Juan Pérez', 'juan.perez@email.com', '+503 6333-4444', 'cliente', 1704067200000),
('cliente004', 'Ana Martínez', 'ana.martinez@email.com', '+503 6555-6666', 'cliente', 1704067200000),
('cliente007', 'Roberto Sánchez', 'roberto.sanchez@email.com', '+503 6777-8888', 'cliente', 1704067200000);

-- Insertar propiedades de prueba
INSERT OR REPLACE INTO propiedades (titulo, descripcion, precio, direccion, latitud, longitud, largo, ancho, area, tipoPropiedad, dormitorios, banos, caracteristicas, vendedorId, fechaPublicacion, estado, medioContacto) VALUES 
('Casa de lujo en Colonia', 'Hermosa casa de lujo de 3 pisos con acabados de primera calidad, jardín amplio y piscina. Ubicada en zona residencial premium de San Salvador.', 450000.0, 'Colonia Escalón, San Salvador', 13.7007, -89.2314, 25.0, 15.0, 375.0, 'casa', 4, 3, 'Piscina, Jardín, Garage para 2 carros, Seguridad 24/7, Área BBQ', 'samuel001', 1704067200000, 'DISPONIBLE', '+503 7123-4567'),

('Apartamento moderno', 'Apartamento moderno de 2 habitaciones en torre residencial con excelentes amenidades y vista panorámica.', 180000.0, 'Torre Futura, San Salvador', 13.6929, -89.2182, 12.0, 8.0, 96.0, 'apartamento', 2, 2, 'Vista panorámica, Gimnasio, Piscina, Seguridad, Ascensor', 'cargueta01', 1704067200000, 'DISPONIBLE', '+503 7234-5678'),

('Local comercial central', 'Excelente local comercial en el centro de la ciudad, ideal para tienda o restaurante.', 320000.0, 'Centro Histórico, San Salvador', 13.6986, -89.1914, 8.0, 6.0, 48.0, 'local_comercial', 0, 1, 'Ubicación céntrica, Amplio escaparate, Baño, Aire acondicionado', 'pendragon503', 1704067200000, 'DISPONIBLE', '+503 7345-6789'),

('Terreno para desarrollo', 'Terreno plano ideal para desarrollo habitacional o comercial en zona de alta plusvalía.', 280000.0, 'Santa Tecla, La Libertad', 13.6774, -89.2797, 50.0, 30.0, 1500.0, 'terreno', 0, 0, 'Plano, Acceso a servicios públicos, Zona comercial', 'eduardo001', 1704067200000, 'DISPONIBLE', '+503 7456-7890'),

('Oficina ejecutiva', 'Elegante oficina ejecutiva en edificio corporativo de primer nivel con todas las comodidades.', 150000.0, 'Torre Empresarial, San Salvador', 13.7012, -89.2245, 8.0, 6.0, 48.0, 'oficina', 0, 1, 'Aire acondicionado, Internet fibra óptica, Recepción, Seguridad', 'samuel001', 1704067200000, 'DISPONIBLE', '+503 7567-8901'),

('Casa familiar', 'Acogedora casa familiar de 2 pisos en barrio tranquilo, perfecta para familias con niños.', 220000.0, 'Colonia Miramonte, San Salvador', 13.6845, -89.2456, 18.0, 12.0, 216.0, 'casa', 3, 2, 'Jardín, Garage, Cerca de escuelas, Área de juegos infantiles', 'cargueta01', 1704067200000, 'DISPONIBLE', '+503 7678-9012'),

('Penthouse de lujo', 'Exclusivo penthouse con terraza en el último piso, acabados de lujo y vistas espectaculares de la ciudad.', 850000.0, 'Torre Premium, San Salvador', 13.7056, -89.2089, 20.0, 15.0, 300.0, 'penthouse', 3, 3, 'Terraza privada, Jacuzzi, Vista 360°, Acabados de lujo, Ascensor privado', 'pendragon503', 1704067200000, 'DISPONIBLE', '+503 4567-8901');

-- Insertar imágenes de propiedades
INSERT OR REPLACE INTO imagenes_propiedad (propiedadId, rutaImagen) VALUES 
(1, '/storage/emulated/0/DCIM/properties/house_luxury_1.jpg'),
(1, '/storage/emulated/0/DCIM/properties/house_luxury_2.jpg'),
(1, '/storage/emulated/0/DCIM/properties/house_luxury_3.jpg'),
(2, '/storage/emulated/0/DCIM/properties/apartment_modern_1.jpg'),
(2, '/storage/emulated/0/DCIM/properties/apartment_modern_2.jpg'),
(3, '/storage/emulated/0/DCIM/properties/commercial_space_1.jpg'),
(3, '/storage/emulated/0/DCIM/properties/commercial_space_2.jpg'),
(4, '/storage/emulated/0/DCIM/properties/land_dev_1.jpg'),
(5, '/storage/emulated/0/DCIM/properties/office_exec_1.jpg'),
(5, '/storage/emulated/0/DCIM/properties/office_exec_2.jpg'),
(6, '/storage/emulated/0/DCIM/properties/family_house_1.jpg'),
(6, '/storage/emulated/0/DCIM/properties/family_house_2.jpg'),
(7, '/storage/emulated/0/DCIM/properties/penthouse_lux_1.jpg'),
(7, '/storage/emulated/0/DCIM/properties/penthouse_lux_2.jpg'),
(7, '/storage/emulated/0/DCIM/properties/penthouse_lux_3.jpg');

-- Insertar visitas de prueba
INSERT OR REPLACE INTO visitas (propiedadId, clienteId, vendedorId, fecha, hora, estado, comentarios) VALUES 
(1, 'cliente001', 'samuel001', 1704153600000, '10:00 AM', 'CONFIRMADA', 'Primera visita para inspeccionar la propiedad'),
(1, 'cliente002', 'samuel001', 1704240000000, '2:00 PM', 'PENDIENTE', 'Cliente interesado en la casa de lujo'),
(3, 'cliente002', 'pendragon503', 1704326400000, '11:00 AM', 'PENDIENTE', 'Excelente ubicación, me interesa mucho'),
(4, 'cliente003', 'eduardo001', 1704412800000, '9:00 AM', 'PENDIENTE', 'Quiero conocer más sobre el terreno'),
(5, 'cliente004', 'samuel001', 1704499200000, '3:00 PM', 'PENDIENTE', 'Evaluar oficina para mi nuevo negocio'),
(6, 'cliente002', 'cargueta01', 1704585600000, '4:00 PM', 'REALIZADA', 'Muy buena propiedad, estoy considerando la compra');

-- =================================================================
-- VERIFICACIÓN DE DATOS INSERTADOS
-- =================================================================

-- Consultas para verificar que los datos se insertaron correctamente
.mode column
.headers on

-- Mostrar usuarios
SELECT 'USUARIOS REGISTRADOS:' as info;
SELECT id, nombre, rol, correo FROM usuarios ORDER BY rol, nombre;

-- Mostrar propiedades
SELECT '' as spacer;
SELECT 'PROPIEDADES DISPONIBLES:' as info;
SELECT id, titulo, tipoPropiedad, precio, estado, vendedorId FROM propiedades ORDER BY precio DESC;

-- Mostrar visitas
SELECT '' as spacer;
SELECT 'VISITAS PROGRAMADAS:' as info;
SELECT v.id, p.titulo as propiedad, u1.nombre as cliente, u2.nombre as vendedor, v.estado 
FROM visitas v
JOIN propiedades p ON v.propiedadId = p.id
JOIN usuarios u1 ON v.clienteId = u1.id
JOIN usuarios u2 ON v.vendedorId = u2.id
ORDER BY v.fecha;

-- Mostrar estadísticas
SELECT '' as spacer;
SELECT 'ESTADÍSTICAS:' as info;
SELECT 
    (SELECT COUNT(*) FROM usuarios WHERE rol = 'vendedor') as vendedores,
    (SELECT COUNT(*) FROM usuarios WHERE rol = 'cliente') as clientes,
    (SELECT COUNT(*) FROM propiedades WHERE estado = 'DISPONIBLE') as propiedades_disponibles,
    (SELECT COUNT(*) FROM visitas WHERE estado = 'PENDIENTE') as visitas_pendientes;

-- =================================================================
-- NOTAS DE IMPLEMENTACIÓN
-- =================================================================

/*
INFORMACIÓN IMPORTANTE:

1. Este archivo está diseñado para ser ejecutado en SQLite (Room Database)
2. Las fechas están almacenadas como timestamps en milisegundos (epoch time)
3. Los precios están en dólares estadounidenses (USD)
4. Las coordenadas están en formato decimal (WGS84)
5. Los estados de propiedades: DISPONIBLE, VENDIDA, RESERVADA
6. Los estados de visitas: PENDIENTE, CONFIRMADA, REALIZADA, CANCELADA
7. Los roles de usuario: admin, vendedor, cliente

COMANDOS PARA IMPORTAR:
- Para crear la base de datos: sqlite3 real_estate_app.db < room_database_setup.sql
- Para Android Studio: Copiar el archivo .db generado a assets/databases/

AUTORES POR MÓDULO:
- Usuarios y Autenticación: Carlos Argueta (@cargueta01)
- Propiedades y Búsqueda: Samuel Alas (@Samuelalas200)
- Favoritos y Perfil: Pendragon503 (@Pendragon503)
- Sistema y Backend: Eduardo Méndez (@EdMendez00)

VERSIÓN: 1.0
FECHA: 15 de Enero de 2025
PROYECTO: Sistema de Venta de Bienes Raíces - PDM115
UNIVERSIDAD DE EL SALVADOR
*/
