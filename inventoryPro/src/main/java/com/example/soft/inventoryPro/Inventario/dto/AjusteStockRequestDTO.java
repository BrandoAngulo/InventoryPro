package com.example.soft.inventoryPro.Inventario.dto;

import com.example.soft.inventoryPro.Inventario.Models.TipoAjuste;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AjusteStockRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "El tipo de ajuste (POSITIVO o NEGATIVO) es obligatorio")
    private TipoAjuste tipoAjuste;

    @NotNull(message = "La cantidad del ajuste es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @PositiveOrZero(message = "El costo unitario de ingreso no puede ser negativo")
    private BigDecimal costoUnitarioIngreso;

    @NotBlank(message = "El motivo del ajuste es obligatorio")
    @Size(min = 5, max = 255, message = "El motivo debe tener entre 5 y 255 caracteres")
    private String motivo;
}
