-- Migration script V3: Convert monetary fields from DECIMAL to BIGINT

ALTER TABLE productos ALTER COLUMN precio_compra TYPE BIGINT USING precio_compra::BIGINT;
ALTER TABLE productos ALTER COLUMN precio_venta TYPE BIGINT USING precio_venta::BIGINT;
ALTER TABLE productos ALTER COLUMN costo_promedio TYPE BIGINT USING costo_promedio::BIGINT;

ALTER TABLE cajas ALTER COLUMN monto_inicial TYPE BIGINT USING monto_inicial::BIGINT;
ALTER TABLE cajas ALTER COLUMN monto_ingresos TYPE BIGINT USING monto_ingresos::BIGINT;
ALTER TABLE cajas ALTER COLUMN monto_egresos TYPE BIGINT USING monto_egresos::BIGINT;
ALTER TABLE cajas ALTER COLUMN monto_esperado TYPE BIGINT USING monto_esperado::BIGINT;
ALTER TABLE cajas ALTER COLUMN monto_cierre TYPE BIGINT USING monto_cierre::BIGINT;
ALTER TABLE cajas ALTER COLUMN diferencia TYPE BIGINT USING diferencia::BIGINT;

ALTER TABLE movimientos_caja ALTER COLUMN monto TYPE BIGINT USING monto::BIGINT;

ALTER TABLE facturas ALTER COLUMN subtotal TYPE BIGINT USING subtotal::BIGINT;
ALTER TABLE facturas ALTER COLUMN impuesto TYPE BIGINT USING impuesto::BIGINT;
ALTER TABLE facturas ALTER COLUMN total TYPE BIGINT USING total::BIGINT;

ALTER TABLE detalles_factura ALTER COLUMN precio_unitario TYPE BIGINT USING precio_unitario::BIGINT;
ALTER TABLE detalles_factura ALTER COLUMN costo_unitario TYPE BIGINT USING costo_unitario::BIGINT;
ALTER TABLE detalles_factura ALTER COLUMN subtotal TYPE BIGINT USING subtotal::BIGINT;

ALTER TABLE pedidos ALTER COLUMN subtotal TYPE BIGINT USING subtotal::BIGINT;
ALTER TABLE pedidos ALTER COLUMN descuento_general TYPE BIGINT USING descuento_general::BIGINT;
ALTER TABLE pedidos ALTER COLUMN impuesto TYPE BIGINT USING impuesto::BIGINT;
ALTER TABLE pedidos ALTER COLUMN total TYPE BIGINT USING total::BIGINT;

ALTER TABLE detalles_pedido ALTER COLUMN precio_lista TYPE BIGINT USING precio_lista::BIGINT;
ALTER TABLE detalles_pedido ALTER COLUMN precio_ajustado TYPE BIGINT USING precio_ajustado::BIGINT;
ALTER TABLE detalles_pedido ALTER COLUMN descuento_unitario TYPE BIGINT USING descuento_unitario::BIGINT;
ALTER TABLE detalles_pedido ALTER COLUMN subtotal_renglon TYPE BIGINT USING subtotal_renglon::BIGINT;

ALTER TABLE compras ALTER COLUMN total TYPE BIGINT USING total::BIGINT;

ALTER TABLE detalles_compra ALTER COLUMN precio_compra TYPE BIGINT USING precio_compra::BIGINT;
ALTER TABLE detalles_compra ALTER COLUMN subtotal TYPE BIGINT USING subtotal::BIGINT;

ALTER TABLE ajustes_inventario ALTER COLUMN costo_unitario_ingreso TYPE BIGINT USING costo_unitario_ingreso::BIGINT;
ALTER TABLE ajustes_inventario ALTER COLUMN costo_promedio_anterior TYPE BIGINT USING costo_promedio_anterior::BIGINT;
ALTER TABLE ajustes_inventario ALTER COLUMN costo_promedio_resultante TYPE BIGINT USING costo_promedio_resultante::BIGINT;

ALTER TABLE movimientos_inventario ALTER COLUMN precio_unitario TYPE BIGINT USING precio_unitario::BIGINT;
ALTER TABLE movimientos_inventario ALTER COLUMN costo_unitario TYPE BIGINT USING costo_unitario::BIGINT;
