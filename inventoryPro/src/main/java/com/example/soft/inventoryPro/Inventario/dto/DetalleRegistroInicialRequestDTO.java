package com.example.soft.inventoryPro.Inventario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleRegistroInicialRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad inicial es obligatoria")
    @Positive(message = "La cantidad ingresada debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El costo unitario de ingreso es obligatorio")
    @PositiveOrZero(message = "El costo unitario de ingreso no puede ser negativo")
    private BigDecimal costoUnitarioIngreso;
}
