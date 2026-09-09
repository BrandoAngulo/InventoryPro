package com.example.soft.inventoryPro.Dashboard.dto;

import com.example.soft.inventoryPro.Compra.dto.CompraResponseDTO;
import com.example.soft.inventoryPro.Productos.dto.ProductoResponseDTO;
import com.example.soft.inventoryPro.venta.dto.FacturaResponseDTO;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResumenDTO {

    /**
     * Monto total de ventas realizadas en el día actual (00:00:00 - 23:59:59)
     */
    private BigDecimal ventasDelDia;

    /**
     * Saldo esperado en la caja abierta actual (montoInicial + ingresos - egresos).
     * Si no hay ninguna caja abierta actualmente, retorna 0.00.
     */
    private BigDecimal totalCajaActual;

    /**
     * Cantidad total de productos cuyo stockActual es menor o igual a su stockMinimo.
     */
    private Long productosBajoStockCount;

    /**
     * Cantidad total de clientes registrados y activos en el sistema.
     */
    private Long clientesActivosCount;

    /**
     * Lista de las últimas 5 facturas emitidas, ordenadas de forma descendente por fecha.
     */
    private List<FacturaResponseDTO> ultimasVentas;

    /**
     * Lista de las últimas 5 compras registradas, ordenadas de forma descendente por fecha.
     */
    private List<CompraResponseDTO> ultimasCompras;

    /**
     * Lista de productos que requieren reabastecimiento (stockActual <= stockMinimo).
     */
    private List<ProductoResponseDTO> productosBajoStock;
}
