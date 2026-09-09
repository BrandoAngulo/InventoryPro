package com.example.soft.inventoryPro.Pedido.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoResponseDTO {
    private Long id;
    private Long productoId;
    private String productoCodigo;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioLista;
    private BigDecimal precioAjustado;
    private BigDecimal descuentoUnitario;
    private BigDecimal subtotalRenglon;
}
