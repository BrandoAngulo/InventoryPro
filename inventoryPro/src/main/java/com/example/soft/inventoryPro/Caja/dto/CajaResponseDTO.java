package com.example.soft.inventoryPro.Caja.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CajaResponseDTO {
    private Long id;
    private BigDecimal montoInicial;
    private BigDecimal montoIngresos;
    private BigDecimal montoEgresos;
    private BigDecimal montoEsperado;
    private BigDecimal montoCierre;
    private BigDecimal diferencia;
    private String estado;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private String observacionApertura;
    private String observacionCierre;
}
