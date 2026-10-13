package com.example.soft.inventoryPro.Pedido.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturarPedidoRequestDTO {

    @NotBlank(message = "La forma de pago es obligatoria (EFECTIVO, TARJETA, TRANSFERENCIA)")
    @Pattern(regexp = "EFECTIVO|TARJETA|TRANSFERENCIA", message = "La forma de pago debe ser EFECTIVO, TARJETA o TRANSFERENCIA")
    private String formaPago;

    @Size(max = 255, message = "La observación no debe superar 255 caracteres")
    private String observacion;
}
