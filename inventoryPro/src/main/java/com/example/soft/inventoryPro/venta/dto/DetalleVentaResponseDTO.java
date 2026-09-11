package com.example.soft.inventoryPro.venta.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaResponseDTO {
    private Long idDetalleVenta;
    private Long idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private Long precioUnitario;
}
