package com.example.soft.inventoryPro.Caja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoCajaRequestDTO {

    @NotBlank(message = "El tipo de movimiento es obligatorio (INGRESO_MANUAL o EGRESO_MANUAL)")
    private String tipo;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser un valor positivo")
    private BigDecimal monto;

    @NotBlank(message = "El concepto o justificación es obligatorio")
    @Size(min = 3, max = 255, message = "El concepto debe tener entre 3 y 255 caracteres")
    private String concepto;
}
