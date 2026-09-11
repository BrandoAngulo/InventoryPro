package com.example.soft.inventoryPro.Pedido.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @PositiveOrZero(message = "El descuento general no puede ser negativo")
    private Long descuentoGeneral;

    @PositiveOrZero(message = "El impuesto no puede ser negativo")
    private Long impuesto;

    @Size(max = 255, message = "La observación no debe superar 255 caracteres")
    private String observacion;

    @NotEmpty(message = "El pedido debe contener al menos un detalle de producto")
    private List<DetallePedidoRequestDTO> detalles;
}
