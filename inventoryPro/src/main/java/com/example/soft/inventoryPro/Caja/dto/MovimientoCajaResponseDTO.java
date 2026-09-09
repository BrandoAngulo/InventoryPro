package com.example.soft.inventoryPro.Caja.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoCajaResponseDTO {
    private Long id;
    private Long cajaId;
    private String tipo;
    private BigDecimal monto;
    private String concepto;
    private Long facturaId;
    private LocalDateTime fecha;
}
