package com.example.soft.inventoryPro.Caja.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AperturaCajaRequestDTO {

    @NotNull(message = "El monto inicial de caja es obligatorio")
    @PositiveOrZero(message = "El monto inicial no puede ser negativo")
    private BigDecimal montoInicial;

    @Size(max = 255, message = "La observación de apertura no debe superar 255 caracteres")
    private String observacion;
}
