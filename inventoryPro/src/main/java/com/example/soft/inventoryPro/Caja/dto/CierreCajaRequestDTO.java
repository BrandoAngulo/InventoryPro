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
public class CierreCajaRequestDTO {

    @NotNull(message = "El monto contado al cierre es obligatorio")
    @PositiveOrZero(message = "El monto de cierre no puede ser negativo")
    private BigDecimal montoCierre;

    @Size(max = 255, message = "La observación de cierre no debe superar 255 caracteres")
    private String observacion;
}
