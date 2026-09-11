package com.example.soft.inventoryPro.Pedido.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad solicitada debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El precio de lista es obligatorio")
    @PositiveOrZero(message = "El precio de lista no puede ser negativo")
    private Long precioLista;

    @NotNull(message = "El precio ajustado es obligatorio")
    @PositiveOrZero(message = "El precio ajustado no puede ser negativo")
    private Long precioAjustado;

    @PositiveOrZero(message = "El descuento unitario no puede ser negativo")
    private Long descuentoUnitario;
}
