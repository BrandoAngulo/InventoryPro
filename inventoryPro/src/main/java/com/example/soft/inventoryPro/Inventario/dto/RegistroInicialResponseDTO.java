package com.example.soft.inventoryPro.Inventario.dto;

import lombok.*;

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
    private Long costoIngreso;
    private Long cppAnterior;
    private Long cppResultante;
    private LocalDateTime fecha;
}
