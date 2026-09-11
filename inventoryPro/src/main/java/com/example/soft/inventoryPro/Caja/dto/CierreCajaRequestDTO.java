package com.example.soft.inventoryPro.Caja.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CierreCajaRequestDTO {

    @NotNull(message = "El monto de cierre es obligatorio")
    @Min(value = 0, message = "El monto de cierre no puede ser negativo")
    private Long montoCierre;

    @Size(max = 255, message = "La observación no debe superar 255 caracteres")
    private String observacion;
}
