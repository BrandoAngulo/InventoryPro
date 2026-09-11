package com.example.soft.inventoryPro.Caja.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AperturaCajaRequestDTO {

    @NotNull(message = "El monto inicial de apertura es obligatorio")
    @Min(value = 0, message = "El monto inicial no puede ser negativo")
    private Long montoInicial;

    @Size(max = 255, message = "La observación no debe superar 255 caracteres")
    private String observacion;
}
