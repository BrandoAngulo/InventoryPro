package com.example.soft.inventoryPro.Stock.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {
    private Long idStock;
    private Long idProducto;
    private String nombreProducto;
    private Integer cantidadStock;
}
