package com.example.soft.inventoryPro.Productos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {

    @NotBlank(message = "El código del producto es obligatorio")
    @Size(min = 2, max = 50, message = "El código debe tener entre 2 y 50 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no debe superar 255 caracteres")
    private String descripcion;

    @NotNull(message = "El precio de venta es obligatorio")
    @Positive(message = "El precio de venta debe ser un número positivo mayores a 0")
    private BigDecimal precioVenta;

    @NotNull(message = "La categoría es obligatoria")
    @Positive(message = "El ID de categoría debe ser válido")
    private Long categoriaId;

    @NotNull(message = "El proveedor es obligatorio")
    @Positive(message = "El ID de proveedor debe ser válido")
    private Long proveedorId;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;

    @Min(value = 0, message = "El stock inicial no puede ser negativo")
    private Integer stockInicial;

    private BigDecimal costoInicial;
}
