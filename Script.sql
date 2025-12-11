CREATE EXTENSION pgcrypto; --PARA ACTIVAR EL MODULO DE ENCRIPTACION
CREATE EXTENSION unaccent;

CREATE TABLE REPUESTO (
    repuesto_id SERIAL NOT NULL,
    nombre_repuesto varchar(100) NOT NULL UNIQUE,
    precio numeric(10, 2) NOT NULL CHECK(precio > 0),
    stock int2 NOT NULL CHECK(stock >= 0),
    fecha_registro date DEFAULT CURRENT_DATE NOT NULL,
    descripcion text,
    estado varchar(20) DEFAULT 'disponible' NOT NULL CHECK(
        estado IN ('disponible', 'agotado', 'descontinuado')
    ),
    marca_id int2 NOT NULL,
    PRIMARY KEY (repuesto_id)
);

CREATE TABLE CLIENTE (
    cliente_id SERIAL NOT NULL,
    nombres varchar(30) NOT NULL,
    apellidos varchar(30),
    tipo varchar(20) NOT NULL CHECK(tipo in ('natural','juridico')), 
    num_doc varchar(25) NOT NULL UNIQUE,
    telefono char(9) CHECK(telefono ~ '^[0-9]{9}$'),
    fecha_registro date DEFAULT CURRENT_DATE NOT NULL,
    estado bool DEFAULT 'TRUE' NOT NULL,
    PRIMARY KEY (cliente_id)
);

CREATE TABLE VEHICULO (
    vehiculo_id SERIAL NOT NULL,
    modelo varchar(50) NOT NULL,
    color varchar(30),
    fecha_registro date DEFAULT CURRENT_DATE NOT NULL,
    num_placa varchar(8) NOT NULL UNIQUE CHECK(num_placa ~ '^[0-9A-Z]{6,8}$'),
    descripcion text,
    estado bool DEFAULT 'TRUE' NOT NULL,
    cliente_id int4 NOT NULL,
    marca_id int2 NOT NULL,
    PRIMARY KEY (vehiculo_id)
);

CREATE TABLE USUARIO (
    usuario_id SERIAL NOT NULL,
    nombres varchar(30) NOT NULL,
    apellidos varchar(30) NOT NULL,
    dni char(8) NOT NULL UNIQUE CHECK(dni ~ '^[0-9]{8}$'),
    clave varchar(180) NOT NULL,
    fecha_registro date DEFAULT CURRENT_DATE NOT NULL,
    telefono char(9) CHECK(telefono ~ '^[0-9]{9}$'),
    estado varchar(20) DEFAULT 'disponible' NOT NULL CHECK(estado IN ('disponible', 'inactivo', 'ocupado')),
    rol varchar(30) NOT NULL CHECK(rol IN ('administrador', 'mecanico')),
    PRIMARY KEY (usuario_id)
);

CREATE TABLE ORDEN (
    orden_id SERIAL NOT NULL,
    fecha_inicio date DEFAULT CURRENT_DATE NOT NULL,
    fecha_fin date CHECK(fecha_fin >= fecha_inicio),
    estado varchar(20) NOT NULL CHECK(
        estado IN (
            'pendiente',
            'en proceso',
            'completado',
            'anulado',
            'pagado'
        )
	),
		fecha_completado date,
		fecha_pago date,
        usuario_id int2,
        vehiculo_id int4 NOT NULL,
        PRIMARY KEY (orden_id)
    );

CREATE TABLE DETALLE_ANULADO(
	idanulado serial NOT NULL,
	fecha_anulador date DEFAULT CURRENT_DATE NOT NULL,
	orden_id int NOT NULL REFERENCES orden(orden_id),
	motivo text NOT NULL,
	PRIMARY KEY(idanulado)
);

CREATE TABLE SERVICIO (
    servicio_id SERIAL NOT NULL,
    nombre_servicio varchar(80) NOT NULL UNIQUE,
    precio_base numeric(10, 2) NOT NULL CHECK(precio_base > 0),
    fecha_registro date DEFAULT CURRENT_DATE NOT NULL,
    estado bool DEFAULT 'TRUE' NOT NULL,
    descripcion text,
    PRIMARY KEY (servicio_id)
);

CREATE TABLE COMPROBANTE (
    comprobante_id SERIAL NOT NULL,
    fecha_pago date DEFAULT CURRENT_DATE NOT NULL,
    monto_total numeric(10, 2) NOT NULL CHECK(monto_total > 0),
    metodo_pago varchar(20) NOT NULL CHECK(metodo_pago IN ('efectivo', 'tarjeta', 'otro')),
    tipo_comprobante varchar(30) NOT NULL CHECK(tipo_comprobante in ('factura', 'boleta')),
    orden_id int4 NOT NULL,
    cliente_id int4 NOT NULL,
    PRIMARY KEY (comprobante_id)
);

CREATE TABLE MARCA (
    marca_id SERIAL NOT NULL,
    nombre_marca varchar(100) NOT NULL UNIQUE,
    fecha_registro date DEFAULT CURRENT_DATE NOT NULL,
    estado bool DEFAULT 'TRUE' NOT NULL,
    descripcion text,
    PRIMARY KEY (marca_id)
);

CREATE TABLE DETALLE_SERVICIO (
    servicio_id int2 NOT NULL,
    orden_id int4 NOT NULL,
    precio numeric(10, 2) NOT NULL CHECK(precio > 0),
    PRIMARY KEY (servicio_id, orden_id)
);

CREATE TABLE DETALLE_REPUESTO (
    orden_id int4 NOT NULL,
    repuesto_id int4 NOT NULL,
    cantidad int2 NOT NULL CHECK(cantidad > 0),
    precio numeric(10, 2) NOT NULL CHECK(precio > 0),
    PRIMARY KEY (orden_id, repuesto_id)
);

ALTER TABLE
    ORDEN
ADD
    CONSTRAINT FKORDEN390649 FOREIGN KEY (usuario_id) REFERENCES USUARIO (usuario_id);

ALTER TABLE
    DETALLE_SERVICIO
ADD
    CONSTRAINT FKDETALLE_SE565923 FOREIGN KEY (servicio_id) REFERENCES SERVICIO (servicio_id);

ALTER TABLE
    DETALLE_SERVICIO
ADD
    CONSTRAINT FKDETALLE_SE396558 FOREIGN KEY (orden_id) REFERENCES ORDEN (orden_id);

ALTER TABLE
    COMPROBANTE
ADD
    CONSTRAINT FKCOMPROBANT880789 FOREIGN KEY (orden_id) REFERENCES ORDEN (orden_id);

ALTER TABLE
    COMPROBANTE
ADD
    CONSTRAINT FKCOMPROBANT490696 FOREIGN KEY (cliente_id) REFERENCES CLIENTE (cliente_id);

ALTER TABLE
    VEHICULO
ADD
    CONSTRAINT FKVEHICULO656281 FOREIGN KEY (cliente_id) REFERENCES CLIENTE (cliente_id);

ALTER TABLE
    DETALLE_REPUESTO
ADD
    CONSTRAINT FKDETALLE_RE700847 FOREIGN KEY (orden_id) REFERENCES ORDEN (orden_id);

ALTER TABLE
    DETALLE_REPUESTO
ADD
    CONSTRAINT FKDETALLE_RE106968 FOREIGN KEY (repuesto_id) REFERENCES REPUESTO (repuesto_id);

ALTER TABLE
    VEHICULO
ADD
    CONSTRAINT FKVEHICULO107445 FOREIGN KEY (marca_id) REFERENCES MARCA (marca_id);

ALTER TABLE
    REPUESTO
ADD
    CONSTRAINT FKREPUESTO761033 FOREIGN KEY (marca_id) REFERENCES MARCA (marca_id);

ALTER TABLE
    ORDEN
ADD
    CONSTRAINT FKORDEN254813 FOREIGN KEY (vehiculo_id) REFERENCES VEHICULO (vehiculo_id);



-- PROCEDIMIENTOS ALMACENADOS
CREATE OR REPLACE FUNCTION eliminar_marca(p_marca_id INT)
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
    rpta INT;
BEGIN
    IF EXISTS (SELECT 1 FROM vehiculo WHERE marca_id = p_marca_id)
       OR EXISTS (SELECT 1 FROM repuesto WHERE marca_id = p_marca_id) THEN
        
        UPDATE marca
        SET estado = false
        WHERE marca_id = p_marca_id;
        
        rpta := 1;
    ELSE
        DELETE FROM marca
        WHERE marca_id = p_marca_id;
        
        rpta := 0; 
    END IF;
    
    RETURN rpta;
END;
$$;

CREATE OR REPLACE FUNCTION eliminar_usuario(p_id_usuario INT) 
RETURNS int LANGUAGE plpgsql AS 
$$ 
DECLARE rpta int; estado_usu varchar;
BEGIN 
	IF EXISTS (SELECT 1 FROM orden WHERE usuario_id = p_id_usuario) THEN
		select estado into estado_usu from usuario where usuario_id=p_id_usuario;
		if estado = 'disponible' then
			rpta = 1;
			UPDATE usuario SET estado = 'inactivo' WHERE usuario_id = p_id_usuario;
		else
			rpta = -1;
		end if;
	ELSE 
		DELETE FROM usuario WHERE usuario_id = p_id_usuario; 
		rpta = 0; 
	END IF; 
RETURN rpta; 
END; 
$$;

--obteber el monto de una orden
create or replace function obtener_monto_orden(id_orden int)
returns numeric language plpgsql as 
$$
declare monto numeric;
begin
	SELECT sum(d_s.precio)+sum(d_r.precio*d_r.cantidad) into monto
	FROM ORDEN o inner join detalle_servicio d_s on d_s.orden_id = o.orden_id
	inner join detalle_repuesto d_r on d_r.orden_id = o.orden_id where o.orden_id=id_orden;
	return monto;
end;
$$;

-- DATOS PARA LA BD

-- MARCA
INSERT INTO MARCA (nombre_marca, descripcion) VALUES
('Toyota', 'Marca japonesa conocida por su fiabilidad.'),
('Honda', 'Fabricante japonés de vehículos y motocicletas.'),
('Ford', 'Marca estadounidense con enfoque en SUVs y camionetas.'),
('Chevrolet', 'Marca americana con modelos variados.'),
('Nissan', 'Marca japonesa con tecnología innovadora.'),
('Hyundai', 'Marca coreana con diseños modernos.');

-- CLIENTE
INSERT INTO CLIENTE (nombres, apellidos, num_doc, tipo, telefono) VALUES
('Juan', 'Pérez Gómez', '12345678', 'natural', '987654321'),
('María', 'López Vargas', '87654321', 'natural', '912345678'),
('Carlos', 'Ramírez Torres', '45678912', 'natural', '923456789'),
('Ana', 'García Flores', '78912345', 'natural', NULL),
('Luis', 'Martínez Rojas', '32165487', 'natural', '934567890'),
('Sofía', 'Cruz Salazar', '14725836', 'natural', '945678901');
INSERT INTO CLIENTE (nombres, apellidos, tipo, num_doc, telefono, fecha_registro, estado) VALUES
('Soluciones Tecnológicas SAC', NULL, 'juridico', '20123456789', '987654321', '2025-01-15', TRUE),
('Comercial Andina SRL', NULL, 'juridico', '20654321098', '912345678', '2025-03-22', TRUE),
('Inversiones Globales EIRL', NULL, 'juridico', '20456789123', '923456789', '2025-06-10', TRUE),
('Constructora del Sur SA', NULL, 'juridico', '20987654321', '934567890', '2025-09-05', TRUE),
('Importadora Lima SAC', NULL, 'juridico', '20789012345', '945678901', '2025-11-01', TRUE);

-- USUARIO
INSERT INTO USUARIO (nombres, apellidos, dni, clave, telefono, rol, estado) VALUES
('Pedro', 'Sánchez Díaz', '11112222', 'admin123', '956789123', 'administrador', 'disponible'),
('Laura', 'Gómez Castro', '22223333', 'admin456', '967890234', 'administrador', 'disponible'),
('Miguel', 'Reyes Luna', '33334444', 'mecanico1', '978901345', 'mecanico', 'ocupado'),
('Elena', 'Vega Ortiz', '44445555', 'mecanico2', NULL, 'mecanico', 'ocupado'),
('Jorge', 'Mendoza Paz', '55556666', 'mecanico3', '989012456', 'mecanico', 'disponible'),
('Clara', 'Ruiz Campos', '66667777', 'mecanico4', '990123567', 'mecanico', 'disponible');
INSERT INTO USUARIO (nombres, apellidos, dni, clave, telefono, rol, estado) VALUES
('Adolfo', 'Perez', '12345678', 'admin123', '987654321', 'administrador', 'disponible');

-- SERVICIO
INSERT INTO SERVICIO (nombre_servicio, precio_base, descripcion) VALUES
('Cambio de aceite', 50.00, 'Cambio de aceite y filtro.'),
('Alineación y balanceo', 80.00, 'Ajuste de ruedas para mejor estabilidad.'),
('Revisión de frenos', 120.00, 'Inspección y ajuste de sistema de frenos.'),
('Cambio de batería', 100.00, 'Reemplazo de batería estándar.'),
('Diagnóstico general', 60.00, 'Revisión completa del vehículo.'),
('Limpieza de inyectores', 90.00, 'Limpieza de sistema de combustible.');

-- VEHICULO
INSERT INTO VEHICULO (modelo, color, num_placa, cliente_id, marca_id, descripcion) VALUES
('Corolla', 'Blanco', 'ABC12345', 1, 1, 'Sedán compacto.'),
('Civic', 'Negro', 'XYZ78912', 2, 2, 'Sedán deportivo.'),
('F-150', 'Azul', 'DEF45678', 3, 3, 'Camioneta robusta.'),
('Cruze', 'Rojo', 'GHI12378', 4, 4, 'Sedán económico.'),
('Sentra', 'Gris', 'JKL45612', 5, 5, 'Sedán familiar.'),
('Tucson', 'Plata', 'MNO78945', 6, 6, 'SUV compacto.'),
('HIACE', 'Blanco', 'TTT100', 7, 1, 'Camioneta robusta.'),
('CRV', 'Gris', 'VVV200', 8, 2, 'Sedán economico.'),
('FOCUS', 'Negro', 'XXX300', 9, 3, 'Vehículo utilitario.'),
('SPARK', 'Blanco', 'YYY400', 10, 4, 'SUV robusto.'),
('QASHQAI', 'Gris', 'ZZZ500', 11, 5, 'Vehículo utilitario');

-- REPUESTO
INSERT INTO REPUESTO (nombre_repuesto, precio, stock, estado, marca_id, descripcion) VALUES
('Filtro de aceite Toyota', 15.00, 50, 'disponible', 1, 'Filtro original para motores Toyota.'),
('Bujías Honda', 20.00, 30, 'disponible', 2, 'Bujías de alto rendimiento.'),
('Pastillas de freno Ford', 45.00, 20, 'disponible', 3, 'Pastillas para F-150.'),
('Batería Chevrolet', 80.00, 10, 'disponible', 4, 'Batería de 12V.'),
('Correa de distribución Nissan', 35.00, 0, 'agotado', 5, 'Correa para motor Nissan.'),
('Filtro de aire Hyundai', 18.00, 40, 'disponible', 6, 'Filtro para SUV Hyundai.'),
('Amortiguadores Toyota', 120.00, 15, 'disponible', 1, 'Juego de amortiguadores delanteros.'),
('Radiador Honda', 150.00, 5, 'disponible', 2, 'Radiador para Civic.'),
('Llantas Ford', 200.00, 0, 'descontinuado', 3, 'Juego de llantas originales.');


-- ORDENES (ya pagadas)
INSERT INTO ORDEN (fecha_inicio, fecha_fin, estado, usuario_id, vehiculo_id)
VALUES
('2024-01-10', '2024-01-11', 'pagado', 3, 1),
('2024-02-15', '2024-02-16', 'pagado', 4, 2),
('2024-03-20', '2024-03-21', 'pagado', 5, 3),
('2024-04-05', '2024-04-07', 'pagado', 3, 4),
('2024-05-18', '2024-05-20', 'pagado', 4, 5),
('2024-06-02', '2024-06-03', 'pagado', 5, 6),
('2024-07-10', '2024-07-12', 'pagado', 3, 1),
('2024-08-14', '2024-08-16', 'pagado', 4, 2),
('2024-09-21', '2024-09-23', 'pagado', 5, 3),
('2024-10-30', '2024-10-31', 'pagado', 3, 4),
('2024-11-08', '2024-11-09', 'pagado', 4, 5),
('2024-12-15', '2024-12-16', 'pagado', 5, 6),

('2025-01-12', '2025-01-14', 'pagado', 3, 1),
('2025-02-19', '2025-02-20', 'pagado', 4, 2),
('2025-03-25', '2025-03-27', 'pagado', 5, 3),
('2025-04-08', '2025-04-09', 'pagado', 3, 4),
('2025-05-17', '2025-05-19', 'pagado', 4, 5),
('2025-06-22', '2025-06-23', 'pagado', 5, 6),
('2025-07-11', '2025-07-12', 'pagado', 3, 1),
('2025-08-19', '2025-08-20', 'pagado', 4, 2),
('2025-09-27', '2025-09-28', 'pagado', 5, 3),
('2025-10-10', '2025-10-11', 'pagado', 3, 4),
('2025-11-15', '2025-11-16', 'pagado', 4, 5),
('2025-12-21', '2025-12-23', 'pagado', 5, 6);
-- ORDENES (pendientes y en proceso)
INSERT INTO ORDEN (fecha_inicio, fecha_fin, estado, usuario_id, vehiculo_id)
VALUES 
    ('2025-11-22', NULL, 'pendiente', NULL, 1),
    ('2025-11-20', NULL, 'pendiente', NULL, 2),
    (CURRENT_DATE, NULL, 'pendiente', NULL, 3);

INSERT INTO ORDEN (fecha_inicio, fecha_fin, fecha_completado, estado, usuario_id, vehiculo_id)
VALUES 
    ('2025-11-22', '2025-11-29', '2025-11-29', 'completado', 3, 1),
    ('2025-11-20', '2025-11-30', '2025-11-30', 'completado', 4, 2);
	
INSERT INTO ORDEN (fecha_inicio, fecha_fin, estado, usuario_id, vehiculo_id)
VALUES
    (
        '2025-11-01',
        (CURRENT_DATE + INTERVAL '2 month') + INTERVAL '3 days',
        'en proceso', 3, 4
    ),
    (
        '2025-11-10',
        (CURRENT_DATE + INTERVAL '1 month') + INTERVAL '10 days',
        'en proceso', 4, 5
    );


-- DETALLE_SERVICIO
INSERT INTO DETALLE_SERVICIO (servicio_id, orden_id, precio) VALUES
(1, 1, 55.00),
(2, 2, 85.00),
(3, 3, 120.00),
(4, 4, 100.00),
(5, 5, 60.00),
(6, 6, 90.00),
(1, 7, 55.00),
(2, 8, 80.00),
(3, 9, 125.00),
(4, 10, 110.00),
(5, 11, 65.00),
(6, 12, 95.00),

(1, 13, 60.00),
(2, 14, 85.00),
(3, 15, 130.00),
(4, 16, 100.00),
(5, 17, 70.00),
(6, 18, 95.00),
(1, 19, 55.00),
(2, 20, 80.00),
(3, 21, 125.00),
(4, 22, 110.00),
(5, 23, 65.00),
(6, 24, 95.00);


-- DETALLE_REPUESTO
INSERT INTO DETALLE_REPUESTO (orden_id, repuesto_id, cantidad, precio) VALUES
(1, 1, 1, 15.00),
(2, 2, 1, 20.00),
(3, 3, 1, 45.00),
(4, 4, 1, 80.00),
(5, 5, 1, 35.00),
(6, 6, 1, 18.00),
(7, 7, 1, 120.00),
(8, 8, 1, 150.00),
(9, 9, 1, 200.00),
(10, 1, 1, 15.00),
(11, 2, 1, 20.00),
(12, 3, 1, 45.00),
(13, 4, 1, 80.00),
(14, 5, 1, 35.00),
(15, 6, 1, 18.00),
(16, 7, 1, 120.00),
(17, 8, 1, 150.00),
(18, 9, 1, 200.00),
(19, 1, 1, 15.00),
(20, 2, 1, 20.00),
(21, 3, 1, 45.00),
(22, 4, 1, 80.00),
(23, 5, 1, 35.00),
(24, 6, 1, 18.00);


-- COMPROBANTE
INSERT INTO COMPROBANTE (fecha_pago, monto_total, metodo_pago, tipo_comprobante, orden_id, cliente_id) VALUES
('2024-01-11', 70.00, 'efectivo', 'boleta', 1, 1),
('2024-02-16', 105.00, 'tarjeta', 'boleta', 2, 2),
('2024-03-21', 165.00, 'efectivo', 'factura', 3, 7),
('2024-04-07', 180.00, 'tarjeta', 'boleta', 4, 4),
('2024-05-20', 95.00, 'efectivo', 'boleta', 5, 5),
('2024-06-03', 108.00, 'efectivo', 'factura', 6, 8),
('2024-07-12', 175.00, 'efectivo', 'boleta', 7, 1),
('2024-08-16', 230.00, 'tarjeta', 'boleta', 8, 2),
('2024-09-23', 325.00, 'efectivo', 'factura', 9, 9),
('2024-10-31', 125.00, 'tarjeta', 'boleta', 10, 4),
('2024-11-09', 85.00, 'efectivo', 'boleta', 11, 5),
('2024-12-16', 140.00, 'efectivo', 'boleta', 12, 6),
('2025-01-14', 95.00, 'efectivo', 'boleta', 13, 1),
('2025-02-20', 115.00, 'tarjeta', 'boleta', 14, 2),
('2025-03-27', 175.00, 'efectivo', 'factura', 15, 10),
('2025-04-09', 180.00, 'tarjeta', 'boleta', 16, 4),
('2025-05-19', 105.00, 'efectivo', 'boleta', 17, 5),
('2025-06-23', 120.00, 'efectivo', 'factura', 18, 11),
('2025-07-12', 165.00, 'efectivo', 'boleta', 19, 1),
('2025-08-20', 230.00, 'tarjeta', 'boleta', 20, 2),
('2025-09-28', 315.00, 'efectivo', 'factura', 21, 7),
('2025-10-11', 125.00, 'tarjeta', 'boleta', 22, 4),
('2025-11-16', 85.00, 'efectivo', 'boleta', 23, 5),
('2025-12-23', 140.00, 'efectivo', 'boleta', 24, 6);


-- PARA LA ENCRIPTACION DE LA CLAVE CON BF
update usuario set clave = crypt(clave,gen_salt('bf'));

