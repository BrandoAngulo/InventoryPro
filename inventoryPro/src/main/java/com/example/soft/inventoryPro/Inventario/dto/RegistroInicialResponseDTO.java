package com.example.soft.inventoryPro.Inventario.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroInicialResponseDTO {
    private Long productoId;
    private String productoCodigo;
    private String productoNombre;
    private Integer stockAnterior;
    private Integer cantidadIngresada;
    private Integer stockResultante;
    private BigDecimal costoIngreso;
    private BigDecimal cppAnterior;
    private BigDecimal cppResultante;
    private LocalDateTime fecha;
}
