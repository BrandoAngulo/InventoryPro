package com.example.soft.inventoryPro.Caja.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CajaResponseDTO {
    private Long id;
    private Long montoInicial;
    private Long montoIngresos;
    private Long montoEgresos;
    private Long montoEsperado;
    private Long montoCierre;
    private Long diferencia;
    private String estado;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private String observacionApertura;
    private String observacionCierre;
}
