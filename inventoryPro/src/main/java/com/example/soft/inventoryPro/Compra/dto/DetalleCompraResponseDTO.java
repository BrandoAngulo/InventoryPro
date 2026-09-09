package com.example.soft.inventoryPro.Compra.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompraResponseDTO {
    private Long id;
    private Long productoId;
    private String productoCodigo;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public Long getIdDetalleCompra() {
        return id;
    }

    public Long getIdProducto() {
        return productoId;
    }

    public String getNombreProducto() {
        return productoNombre;
    }

    public Double getPrecioCompra() {
        return precioUnitario != null ? precioUnitario.doubleValue() : null;
    }
}
