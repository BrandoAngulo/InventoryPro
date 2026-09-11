package com.example.soft.inventoryPro.Pedido.dto;

import lombok.*;

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
    private Long precioLista;
    private Long precioAjustado;
    private Long descuentoUnitario;
    private Long subtotalRenglon;
}
