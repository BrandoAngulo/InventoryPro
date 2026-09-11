package com.example.soft.inventoryPro.Caja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoCajaRequestDTO {

    @NotBlank(message = "El tipo de movimiento es obligatorio (INGRESO_MANUAL, EGRESO_MANUAL)")
    private String tipo;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser un valor mayor a cero")
    private Long monto;

    @NotBlank(message = "El concepto es obligatorio")
    @Size(max = 255, message = "El concepto no debe superar 255 caracteres")
    private String concepto;
}
