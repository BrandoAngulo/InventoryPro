package com.example.soft.inventoryPro.Compra.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompraRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    private Long idProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad comprada debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El precio unitario de compra es obligatorio")
    @Positive(message = "El precio unitario de compra debe ser mayor a 0")
    private Long precioUnitario;

    private Long precioCompra;

    public Long getProductoId() {
        return productoId != null ? productoId : idProducto;
    }

    public Long getPrecioUnitario() {
        if (precioUnitario != null) {
            return precioUnitario;
        }
        return precioCompra;
    }
}
