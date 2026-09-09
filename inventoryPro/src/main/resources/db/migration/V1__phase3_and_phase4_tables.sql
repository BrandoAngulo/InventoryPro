-- Migration script for Phase 3 and Phase 4 tables

CREATE TABLE IF NOT EXISTS movimientos_inventario (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    costo_unitario DECIMAL(12,2) NOT NULL,
    fecha TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    referencia_id BIGINT,
    observacion VARCHAR(255),
    CONSTRAINT fk_movimiento_producto FOREIGN KEY (producto_id) REFERENCES productos(id_productos)
);

CREATE INDEX IF NOT EXISTS idx_mov_producto_fecha ON movimientos_inventario(producto_id, fecha DESC);

CREATE TABLE IF NOT EXISTS facturas (
    id BIGSERIAL PRIMARY KEY,
    numero_factura VARCHAR(50) NOT NULL UNIQUE,
    cliente_id BIGINT NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    impuesto DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(12,2) NOT NULL,
    forma_pago VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PAGADA',
    fecha TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    observacion VARCHAR(255),
    CONSTRAINT fk_factura_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id_cliente)
);

CREATE TABLE IF NOT EXISTS detalles_factura (
    id BIGSERIAL PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    costo_unitario DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_detalle_factura FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos(id_productos)
);

CREATE TABLE IF NOT EXISTS cajas (
    id BIGSERIAL PRIMARY KEY,
    monto_inicial DECIMAL(12,2) NOT NULL,
    monto_ingresos DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    monto_egresos DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    monto_esperado DECIMAL(12,2) NOT NULL,
    monto_cierre DECIMAL(12,2),
    diferencia DECIMAL(12,2),
    estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTA',
    fecha_apertura TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_cierre TIMESTAMP WITH TIME ZONE,
    observacion_apertura VARCHAR(255),
    observacion_cierre VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS movimientos_caja (
    id BIGSERIAL PRIMARY KEY,
    caja_id BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    concepto VARCHAR(255) NOT NULL,
    factura_id BIGINT,
    fecha TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_mov_caja_caja FOREIGN KEY (caja_id) REFERENCES cajas(id),
    CONSTRAINT fk_mov_caja_factura FOREIGN KEY (factura_id) REFERENCES facturas(id)
);
