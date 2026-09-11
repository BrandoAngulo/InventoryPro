package com.example.soft.inventoryPro.Compra.dto;

import lombok.*;

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
    private Long precioUnitario;
    private Long subtotal;

    public Long getIdDetalleCompra() {
        return id;
    }

    public Long getIdProducto() {
        return productoId;
    }

    public String getNombreProveedor() {
        return productoNombre;
    }

    public Long getPrecioCompra() {
        return precioUnitario;
    }
}
