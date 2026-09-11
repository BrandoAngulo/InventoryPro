package com.example.soft.inventoryPro.venta.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleFacturaResponseDTO {
    private Long id;
    private Long productoId;
    private String productoCodigo;
    private String productoNombre;
    private Integer cantidad;
    private Long precioUnitario;
    private Long costoUnitario;
    private Long subtotal;
}
