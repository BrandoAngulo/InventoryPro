-- Migration script for Pedidos and DetallesPedido tables

CREATE TABLE IF NOT EXISTS pedidos (
    id BIGSERIAL PRIMARY KEY,
    numero_pedido VARCHAR(50) NOT NULL UNIQUE,
    cliente_id BIGINT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    subtotal BIGINT NOT NULL CONSTRAINT chk_ped_subtotal CHECK (subtotal >= 0),
    descuento_general BIGINT NOT NULL DEFAULT 0 CONSTRAINT chk_ped_desc_gen CHECK (descuento_general >= 0),
    impuesto BIGINT NOT NULL DEFAULT 0 CONSTRAINT chk_ped_impuesto CHECK (impuesto >= 0),
    total BIGINT NOT NULL CONSTRAINT chk_ped_total CHECK (total >= 0),
    observacion VARCHAR(255),
    fecha_creacion TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id_cliente),
    CONSTRAINT chk_estado_pedido CHECK (estado IN ('PENDIENTE', 'CONFIRMADO', 'FACTURADO', 'CANCELADO'))
);

CREATE TABLE IF NOT EXISTS detalles_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL CONSTRAINT chk_detped_cant CHECK (cantidad > 0),
    precio_lista BIGINT NOT NULL CONSTRAINT chk_detped_plista CHECK (precio_lista >= 0),
    precio_ajustado BIGINT NOT NULL CONSTRAINT chk_detped_pajust CHECK (precio_ajustado >= 0),
    descuento_unitario BIGINT NOT NULL DEFAULT 0 CONSTRAINT chk_detped_dunit CHECK (descuento_unitario >= 0),
    subtotal_renglon BIGINT NOT NULL CONSTRAINT chk_detped_subren CHECK (subtotal_renglon >= 0),
    CONSTRAINT fk_detped_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_detped_producto FOREIGN KEY (producto_id) REFERENCES productos(id_productos)
);

CREATE INDEX IF NOT EXISTS idx_pedidos_cliente ON pedidos(cliente_id);
CREATE INDEX IF NOT EXISTS idx_pedidos_estado ON pedidos(estado);
