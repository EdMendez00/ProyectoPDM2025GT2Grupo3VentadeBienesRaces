-- =================================================================
-- SCHEMA DE BASE DE DATOS - SISTEMA VENTA DE BIENES RAÍCES
-- Universidad de El Salvador - PDM115
-- Grupo Teórico 02 - Grupo de Proyecto 03
-- =================================================================

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS ventabienesraices_db
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ventabienesraices_db;

-- =================================================================
-- TABLA: usuarios
-- Descripción: Almacena información de usuarios del sistema
-- Autores: Carlos Argueta, Samuel Alas, Pendragon503, Eduardo Méndez
-- =================================================================

CREATE TABLE usuarios (
    id VARCHAR(255) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255),
    correo VARCHAR(255) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    rol ENUM('cliente', 'vendedor', 'admin') NOT NULL DEFAULT 'cliente',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    firebase_uid VARCHAR(255) UNIQUE,
    foto_perfil TEXT,
    direccion TEXT,
    fecha_nacimiento DATE,
    genero ENUM('masculino', 'femenino', 'otro'),
    verificado BOOLEAN DEFAULT FALSE,
    ultimo_acceso TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_correo (correo),
    INDEX idx_rol (rol),
    INDEX idx_firebase_uid (firebase_uid),
    INDEX idx_activo (activo)
) ENGINE=InnoDB;

-- =================================================================
-- TABLA: propiedades
-- Descripción: Almacena información de propiedades inmobiliarias
-- Autores: Samuel Alas, Carlos Argueta, Pendragon503
-- =================================================================

CREATE TABLE propiedades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    precio DECIMAL(15,2) NOT NULL,
    direccion TEXT NOT NULL,
    latitud DECIMAL(10,8),
    longitud DECIMAL(11,8),
    largo DECIMAL(8,2),
    ancho DECIMAL(8,2),
    area DECIMAL(10,2),
    tipo_propiedad ENUM('casa', 'apartamento', 'local_comercial', 'terreno', 'oficina', 'bodega') NOT NULL,
    dormitorios INT DEFAULT 0,
    banos INT DEFAULT 0,
    cocheras INT DEFAULT 0,
    pisos INT DEFAULT 1,
    caracteristicas TEXT, -- JSON o texto separado por comas
    imagenes TEXT, -- URLs separadas por comas
    imagen_principal TEXT,
    usuario_id VARCHAR(255) NOT NULL,
    fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    estado ENUM('DISPONIBLE', 'VENDIDA', 'RESERVADA', 'SUSPENDIDA') DEFAULT 'DISPONIBLE',
    medio_contacto TEXT,
    destacada BOOLEAN DEFAULT FALSE,
    vistas INT DEFAULT 0,
    me_gusta INT DEFAULT 0,
    precio_negociable BOOLEAN DEFAULT TRUE,
    amueblada BOOLEAN DEFAULT FALSE,
    mascotas_permitidas BOOLEAN DEFAULT FALSE,
    servicios_incluidos TEXT, -- agua, luz, internet, etc.
    condicion_propiedad ENUM('nueva', 'usada', 'en_construccion', 'remodelada') DEFAULT 'usada',
    ano_construccion YEAR,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_tipo_propiedad (tipo_propiedad),
    INDEX idx_precio (precio),
    INDEX idx_estado (estado),
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_fecha_publicacion (fecha_publicacion),
    INDEX idx_ubicacion (latitud, longitud),
    INDEX idx_destacada (destacada),
    FULLTEXT(titulo, descripcion, direccion)
) ENGINE=InnoDB;

-- =================================================================
-- TABLA: imagenes_propiedad
-- Descripción: Almacena las imágenes de cada propiedad
-- Autores: Samuel Alas, Carlos Argueta
-- =================================================================

CREATE TABLE imagenes_propiedad (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    propiedad_id BIGINT NOT NULL,
    url TEXT NOT NULL,
    descripcion VARCHAR(255),
    es_principal BOOLEAN DEFAULT FALSE,
    orden_visualizacion INT DEFAULT 0,
    tamaño_archivo BIGINT, -- en bytes
    formato_imagen VARCHAR(10), -- jpg, png, webp
    ancho_pixels INT,
    alto_pixels INT,
    subida_por VARCHAR(255),
    fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN DEFAULT TRUE,
    
    FOREIGN KEY (propiedad_id) REFERENCES propiedades(id) ON DELETE CASCADE,
    FOREIGN KEY (subida_por) REFERENCES usuarios(id) ON DELETE SET NULL,
    INDEX idx_propiedad_id (propiedad_id),
    INDEX idx_es_principal (es_principal),
    INDEX idx_orden (orden_visualizacion)
) ENGINE=InnoDB;

-- =================================================================
-- TABLA: visitas
-- Descripción: Programa y registra visitas a propiedades
-- Autores: Pendragon503, Samuel Alas
-- =================================================================

CREATE TABLE visitas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    propiedad_id BIGINT NOT NULL,
    cliente_id VARCHAR(255) NOT NULL,
    vendedor_id VARCHAR(255) NOT NULL,
    fecha_visita DATE NOT NULL,
    hora_visita TIME NOT NULL,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('programada', 'confirmada', 'realizada', 'cancelada', 'no_asistio') DEFAULT 'programada',
    notas_cliente TEXT,
    notas_vendedor TEXT,
    calificacion_cliente TINYINT CHECK (calificacion_cliente BETWEEN 1 AND 5),
    calificacion_vendedor TINYINT CHECK (calificacion_vendedor BETWEEN 1 AND 5),
    comentarios_post_visita TEXT,
    motivo_cancelacion TEXT,
    telefono_contacto VARCHAR(20),
    email_contacto VARCHAR(255),
    recordatorio_enviado BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (propiedad_id) REFERENCES propiedades(id) ON DELETE CASCADE,
    FOREIGN KEY (cliente_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (vendedor_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_propiedad_id (propiedad_id),
    INDEX idx_cliente_id (cliente_id),
    INDEX idx_vendedor_id (vendedor_id),
    INDEX idx_fecha_visita (fecha_visita),
    INDEX idx_estado (estado),
    UNIQUE KEY unique_visita (propiedad_id, cliente_id, fecha_visita, hora_visita)
) ENGINE=InnoDB;

-- =================================================================
-- TABLA: favoritos
-- Descripción: Almacena las propiedades favoritas de cada usuario
-- Autores: Pendragon503, Carlos Argueta
-- =================================================================

CREATE TABLE favoritos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id VARCHAR(255) NOT NULL,
    propiedad_id BIGINT NOT NULL,
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    notas_personales TEXT,
    
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (propiedad_id) REFERENCES propiedades(id) ON DELETE CASCADE,
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_propiedad_id (propiedad_id),
    INDEX idx_fecha_agregado (fecha_agregado),
    UNIQUE KEY unique_favorito (usuario_id, propiedad_id)
) ENGINE=InnoDB;

-- =================================================================
-- TABLA: busquedas_guardadas
-- Descripción: Almacena las búsquedas guardadas por los usuarios
-- Autores: Samuel Alas, Carlos Argueta
-- =================================================================

CREATE TABLE busquedas_guardadas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id VARCHAR(255) NOT NULL,
    nombre_busqueda VARCHAR(255) NOT NULL,
    criterios_busqueda JSON NOT NULL,
    alertas_activas BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_ejecucion TIMESTAMP,
    numero_resultados INT DEFAULT 0,
    
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_alertas_activas (alertas_activas)
) ENGINE=InnoDB;

-- =================================================================
-- TABLA: mensajes
-- Descripción: Sistema de mensajería entre usuarios
-- Autores: Eduardo Méndez, Pendragon503
-- =================================================================

CREATE TABLE mensajes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    remitente_id VARCHAR(255) NOT NULL,
    destinatario_id VARCHAR(255) NOT NULL,
    propiedad_id BIGINT,
    asunto VARCHAR(255),
    mensaje TEXT NOT NULL,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    leido BOOLEAN DEFAULT FALSE,
    fecha_lectura TIMESTAMP NULL,
    tipo_mensaje ENUM('consulta', 'oferta', 'general', 'sistema') DEFAULT 'general',
    archivo_adjunto TEXT,
    eliminado_remitente BOOLEAN DEFAULT FALSE,
    eliminado_destinatario BOOLEAN DEFAULT FALSE,
    
    FOREIGN KEY (remitente_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (destinatario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (propiedad_id) REFERENCES propiedades(id) ON DELETE SET NULL,
    INDEX idx_remitente (remitente_id),
    INDEX idx_destinatario (destinatario_id),
    INDEX idx_propiedad (propiedad_id),
    INDEX idx_fecha_envio (fecha_envio),
    INDEX idx_leido (leido)
) ENGINE=InnoDB;

-- =================================================================
-- TABLA: reportes
-- Descripción: Sistema de reportes de propiedades o usuarios
-- Autores: Eduardo Méndez, Carlos Argueta
-- =================================================================

CREATE TABLE reportes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_reporta_id VARCHAR(255) NOT NULL,
    tipo_entidad ENUM('propiedad', 'usuario') NOT NULL,
    entidad_id VARCHAR(255) NOT NULL, -- ID de la propiedad o usuario reportado
    motivo ENUM('contenido_inapropiado', 'informacion_falsa', 'spam', 'fraude', 'otro') NOT NULL,
    descripcion TEXT,
    estado ENUM('pendiente', 'en_revision', 'resuelto', 'rechazado') DEFAULT 'pendiente',
    fecha_reporte TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_resolucion TIMESTAMP NULL,
    resuelto_por VARCHAR(255),
    accion_tomada TEXT,
    
    FOREIGN KEY (usuario_reporta_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (resuelto_por) REFERENCES usuarios(id) ON DELETE SET NULL,
    INDEX idx_usuario_reporta (usuario_reporta_id),
    INDEX idx_tipo_entidad (tipo_entidad),
    INDEX idx_entidad_id (entidad_id),
    INDEX idx_estado (estado),
    INDEX idx_fecha_reporte (fecha_reporte)
) ENGINE=InnoDB;

-- =================================================================
-- TABLA: configuraciones_usuario
-- Descripción: Configuraciones personalizadas de cada usuario
-- Autores: Pendragon503, Eduardo Méndez
-- =================================================================

CREATE TABLE configuraciones_usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id VARCHAR(255) NOT NULL,
    notificaciones_email BOOLEAN DEFAULT TRUE,
    notificaciones_push BOOLEAN DEFAULT TRUE,
    notificaciones_nuevas_propiedades BOOLEAN DEFAULT TRUE,
    notificaciones_cambios_precio BOOLEAN DEFAULT TRUE,
    notificaciones_mensajes BOOLEAN DEFAULT TRUE,
    idioma VARCHAR(5) DEFAULT 'es',
    zona_horaria VARCHAR(50) DEFAULT 'America/El_Salvador',
    moneda VARCHAR(3) DEFAULT 'USD',
    radio_busqueda_km INT DEFAULT 50,
    configuraciones_privacidad JSON,
    tema_aplicacion ENUM('claro', 'oscuro', 'automatico') DEFAULT 'claro',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    UNIQUE KEY unique_config_usuario (usuario_id)
) ENGINE=InnoDB;

-- =================================================================
-- TABLA: estadisticas_propiedades
-- Descripción: Estadísticas de visualización y engagement
-- Autores: Eduardo Méndez, Samuel Alas
-- =================================================================

CREATE TABLE estadisticas_propiedades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    propiedad_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    visualizaciones INT DEFAULT 0,
    clicks_telefono INT DEFAULT 0,
    clicks_email INT DEFAULT 0,
    clicks_whatsapp INT DEFAULT 0,
    favoritos_agregados INT DEFAULT 0,
    favoritos_removidos INT DEFAULT 0,
    visitas_programadas INT DEFAULT 0,
    compartidos INT DEFAULT 0,
    
    FOREIGN KEY (propiedad_id) REFERENCES propiedades(id) ON DELETE CASCADE,
    INDEX idx_propiedad_fecha (propiedad_id, fecha),
    INDEX idx_fecha (fecha),
    UNIQUE KEY unique_stats_daily (propiedad_id, fecha)
) ENGINE=InnoDB;

-- =================================================================
-- TABLA: logs_actividad
-- Descripción: Registro de actividades del sistema para auditoría
-- Autores: Eduardo Méndez, Carlos Argueta
-- =================================================================

CREATE TABLE logs_actividad (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id VARCHAR(255),
    accion VARCHAR(100) NOT NULL,
    tabla_afectada VARCHAR(50),
    registro_id VARCHAR(255),
    datos_anteriores JSON,
    datos_nuevos JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    fecha_accion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    exito BOOLEAN DEFAULT TRUE,
    mensaje_error TEXT,
    
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL,
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_accion (accion),
    INDEX idx_tabla_afectada (tabla_afectada),
    INDEX idx_fecha_accion (fecha_accion),
    INDEX idx_exito (exito)
) ENGINE=InnoDB;

-- =================================================================
-- TRIGGERS PARA AUDITORÍA Y MANTENIMIENTO
-- =================================================================

-- Trigger para actualizar contador de vistas en propiedades
DELIMITER //
CREATE TRIGGER actualizar_vistas_propiedad
    AFTER INSERT ON estadisticas_propiedades
    FOR EACH ROW
BEGIN
    UPDATE propiedades 
    SET vistas = vistas + NEW.visualizaciones 
    WHERE id = NEW.propiedad_id;
END//
DELIMITER ;

-- Trigger para log de cambios en propiedades
DELIMITER //
CREATE TRIGGER log_cambios_propiedades
    AFTER UPDATE ON propiedades
    FOR EACH ROW
BEGIN
    INSERT INTO logs_actividad (
        usuario_id, accion, tabla_afectada, registro_id, 
        datos_anteriores, datos_nuevos, fecha_accion
    ) VALUES (
        NEW.usuario_id, 'UPDATE', 'propiedades', NEW.id,
        JSON_OBJECT(
            'titulo', OLD.titulo, 'precio', OLD.precio, 'estado', OLD.estado,
            'descripcion', OLD.descripcion, 'tipo_propiedad', OLD.tipo_propiedad
        ),
        JSON_OBJECT(
            'titulo', NEW.titulo, 'precio', NEW.precio, 'estado', NEW.estado,
            'descripcion', NEW.descripcion, 'tipo_propiedad', NEW.tipo_propiedad
        ),
        NOW()
    );
END//
DELIMITER ;

-- =================================================================
-- VISTAS ÚTILES PARA CONSULTAS FRECUENTES
-- =================================================================

-- Vista de propiedades con información del vendedor
CREATE VIEW vista_propiedades_completa AS
SELECT 
    p.id, p.titulo, p.descripcion, p.precio, p.direccion,
    p.latitud, p.longitud, p.tipo_propiedad, p.dormitorios, p.banos,
    p.area, p.estado, p.fecha_publicacion, p.vistas, p.destacada,
    p.imagen_principal,
    u.nombre as vendedor_nombre, u.telefono as vendedor_telefono,
    u.correo as vendedor_correo, u.foto_perfil as vendedor_foto,
    COUNT(f.id) as total_favoritos,
    COUNT(v.id) as total_visitas_programadas
FROM propiedades p
LEFT JOIN usuarios u ON p.usuario_id = u.id
LEFT JOIN favoritos f ON p.id = f.propiedad_id AND f.activo = TRUE
LEFT JOIN visitas v ON p.id = v.propiedad_id AND v.estado IN ('programada', 'confirmada')
WHERE p.estado = 'DISPONIBLE' AND u.activo = TRUE
GROUP BY p.id;

-- Vista de estadísticas por usuario vendedor
CREATE VIEW estadisticas_vendedor AS
SELECT 
    u.id, u.nombre, u.correo,
    COUNT(p.id) as total_propiedades,
    COUNT(CASE WHEN p.estado = 'DISPONIBLE' THEN 1 END) as propiedades_disponibles,
    COUNT(CASE WHEN p.estado = 'VENDIDA' THEN 1 END) as propiedades_vendidas,
    AVG(p.precio) as precio_promedio,
    SUM(p.vistas) as total_vistas,
    COUNT(f.id) as total_favoritos_recibidos,
    COUNT(v.id) as total_visitas_programadas
FROM usuarios u
LEFT JOIN propiedades p ON u.id = p.usuario_id
LEFT JOIN favoritos f ON p.id = f.propiedad_id AND f.activo = TRUE
LEFT JOIN visitas v ON p.id = v.propiedad_id
WHERE u.rol IN ('vendedor', 'admin')
GROUP BY u.id;

-- =================================================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- =================================================================

-- Índices compuestos para consultas frecuentes
CREATE INDEX idx_propiedades_busqueda ON propiedades(tipo_propiedad, precio, estado, fecha_publicacion);
CREATE INDEX idx_propiedades_ubicacion_precio ON propiedades(latitud, longitud, precio, estado);
CREATE INDEX idx_visitas_fecha_estado ON visitas(fecha_visita, estado, vendedor_id);
CREATE INDEX idx_favoritos_usuario_activo ON favoritos(usuario_id, activo, fecha_agregado);

-- =================================================================
-- COMENTARIOS FINALES
-- =================================================================

/*
NOTAS DE IMPLEMENTACIÓN:

1. Esta estructura está optimizada para Android con Room Database
2. Los campos JSON requieren MySQL 5.7+ o MariaDB 10.2+
3. Las coordenadas están en formato decimal (WGS84)
4. Los precios están en USD por defecto
5. Todas las fechas están en UTC
6. Los triggers requieren permisos SUPER en MySQL

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
