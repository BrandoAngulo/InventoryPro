package com.example.soft.inventoryPro.Pedido.dto;

import com.example.soft.inventoryPro.Pedido.Models.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CambioEstadoPedidoDTO {

    @NotNull(message = "El nuevo estado es obligatorio (CONFIRMADO o CANCELADO)")
    private EstadoPedido nuevoEstado;
}
