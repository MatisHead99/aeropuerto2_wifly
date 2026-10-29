-- Base de datos para Sistema de Gestión de Aeropuerto
-- Crear base de datos
CREATE DATABASE IF NOT EXISTS aeropuerto;
USE aeropuerto;

-- Tabla de Vuelos
CREATE TABLE vuelos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    destino VARCHAR(100) NOT NULL,
    origen VARCHAR(100) NOT NULL,
    hora_salida DATETIME NOT NULL,
    hora_llegada DATETIME NOT NULL,
    aerolinea VARCHAR(100) NOT NULL,
    puerta_salida VARCHAR(10),
    capacidad INT NOT NULL DEFAULT 180,
    pasajeros INT NOT NULL DEFAULT 0,
    estado VARCHAR(20) DEFAULT 'Activo',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_codigo (codigo),
    INDEX idx_estado (estado),
    INDEX idx_hora_salida (hora_salida)
);

-- Tabla de Tiquetes
CREATE TABLE tiquetes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(15) NOT NULL UNIQUE,
    vuelo_id INT NOT NULL,
    pasajero VARCHAR(150) NOT NULL,
    asiento VARCHAR(10),
    precio DECIMAL(10, 2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'Activo',
    fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vuelo_id) REFERENCES vuelos(id) ON DELETE CASCADE,
    INDEX idx_numero (numero),
    INDEX idx_vuelo (vuelo_id),
    INDEX idx_estado (estado)
);

-- Tabla de Personal
CREATE TABLE personal (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    cedula VARCHAR(20) NOT NULL UNIQUE,
    puesto VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(100) NOT NULL,
    estado VARCHAR(20) DEFAULT 'Activo',
    fecha_ingreso DATETIME NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_usuario (usuario),
    INDEX idx_cedula (cedula)
);

-- Tabla de Asignaciones (Personal asignado a Vuelos)
CREATE TABLE asignaciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    vuelo_id INT NOT NULL,
    personal_id INT NOT NULL,
    rol VARCHAR(100) NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vuelo_id) REFERENCES vuelos(id) ON DELETE CASCADE,
    FOREIGN KEY (personal_id) REFERENCES personal(id) ON DELETE CASCADE,
    UNIQUE KEY unique_asignacion (vuelo_id, personal_id, rol),
    INDEX idx_vuelo (vuelo_id),
    INDEX idx_personal (personal_id)
);

-- Tabla de Auditoría (opcional, para logs)
CREATE TABLE auditoria (
    id INT PRIMARY KEY AUTO_INCREMENT,
    personal_id INT,
    accion VARCHAR(100) NOT NULL,
    tabla_afectada VARCHAR(100),
    registro_id INT,
    descripcion TEXT,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (personal_id) REFERENCES personal(id),
    INDEX idx_fecha (fecha_hora),
    INDEX idx_personal (personal_id)
);

-- =====================================================
-- DATOS DE PRUEBA
-- =====================================================

-- Personal (usuario: admin / contraseña: admin123)
INSERT INTO personal (nombre, cedula, puesto, telefono, email, usuario, contrasena, estado, fecha_ingreso)
VALUES
('Administrador General', '1234567890', 'Administrador', '3001234567', 'admin@aeropuerto.com', 'admin', 'admin123', 'Activo', NOW()),
('Carlos Rodríguez', '9876543210', 'Supervisor de Vuelos', '3009876543', 'carlos@aeropuerto.com', 'carlos', 'carlos123', 'Activo', NOW()),
('María González', '1122334455', 'Agente de Tiquetes', '3001122334', 'maria@aeropuerto.com', 'maria', 'maria123', 'Activo', NOW()),
('Juan Martínez', '5566778899', 'Capitán', '3005566778', 'juan@aeropuerto.com', 'juan', 'juan123', 'Activo', NOW());

-- Vuelos de prueba
INSERT INTO vuelos (codigo, destino, origen, hora_salida, hora_llegada, aerolinea, puerta_salida, capacidad, pasajeros, estado)
VALUES
('AA101', 'Miami', 'Bogotá', DATE_ADD(NOW(), INTERVAL 2 HOUR), DATE_ADD(NOW(), INTERVAL 5 HOUR), 'American Airlines', 'A10', 180, 142, 'Activo'),
('UA202', 'New York', 'Bogotá', DATE_ADD(NOW(), INTERVAL 3 HOUR), DATE_ADD(NOW(), INTERVAL 6 HOUR), 'United Airlines', 'A15', 200, 195, 'Activo'),
('AV303', 'Cartagena', 'Bogotá', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR), 'Avianca', 'B5', 140, 85, 'Activo'),
('LH404', 'Frankfurt', 'Bogotá', DATE_ADD(NOW(), INTERVAL 4 HOUR), DATE_ADD(NOW(), INTERVAL 12 HOUR), 'Lufthansa', 'C20', 250, 220, 'Activo'),
('AV505', 'Medellín', 'Bogotá', DATE_ADD(NOW(), INTERVAL 45 MINUTE), DATE_ADD(NOW(), INTERVAL 90 MINUTE), 'Avianca', 'B8', 100, 76, 'Activo');

-- Tiquetes de prueba
INSERT INTO tiquetes (numero, vuelo_id, pasajero, asiento, precio, estado)
VALUES
('TICKET001', 1, 'Pedro López', '12A', 450.00, 'Activo'),
('TICKET002', 1, 'Ana Martínez', '12B', 450.00, 'Activo'),
('TICKET003', 1, 'Luis García', '13A', 480.00, 'Activo'),
('TICKET004', 2, 'Carmen Ruiz', '15C', 520.00, 'Activo'),
('TICKET005', 2, 'Felipe Torres', '15D', 520.00, 'Activo'),
('TICKET006', 3, 'Sofía Díaz', '5A', 120.00, 'Activo'),
('TICKET007', 3, 'Diego Castro', '5B', 120.00, 'Activo'),
('TICKET008', 4, 'Isabella Romero', '22A', 850.00, 'Activo'),
('TICKET009', 5, 'Andrés Vargas', '8A', 150.00, 'Activo');

-- Asignaciones
INSERT INTO asignaciones (vuelo_id, personal_id, rol)
VALUES
(1, 4, 'Capitán'),
(1, 3, 'Agente de Embarque'),
(2, 4, 'Capitán'),
(3, 3, 'Agente de Embarque'),
(4, 4, 'Capitán'),
(5, 4, 'Capitán');

-- =====================================================
-- CONSULTAS ÚTILES PARA CONSULTAR DATOS
-- =====================================================
-- Vuelos activos ordenados por hora de salida:
-- SELECT * FROM vuelos WHERE estado = 'Activo' ORDER BY hora_salida ASC;

-- Tiquetes vendidos por vuelo:
-- SELECT v.codigo, COUNT(t.id) as tiquetes_vendidos, v.capacidad, COUNT(t.id) as asientos_ocupados
-- FROM vuelos v
-- LEFT JOIN tiquetes t ON v.id = t.vuelo_id
-- GROUP BY v.id;

-- Personal activo por puesto:
-- SELECT puesto, COUNT(*) as cantidad FROM personal WHERE estado = 'Activo' GROUP BY puesto;

-- Ingresos por vuelo:
-- SELECT v.codigo, v.destino, COUNT(t.id) as tiquetes_vendidos, SUM(t.precio) as ingresos_totales
-- FROM vuelos v
-- LEFT JOIN tiquetes t ON v.id = t.vuelo_id
-- GROUP BY v.id;
