package com.example.soft.inventoryPro.Caja.Models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cajas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Caja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monto_inicial", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoInicial;

    @Column(name = "monto_ingresos", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoIngresos;

    @Column(name = "monto_egresos", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoEgresos;

    @Column(name = "monto_esperado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoEsperado;

    @Column(name = "monto_cierre", precision = 12, scale = 2)
    private BigDecimal montoCierre;

    @Column(precision = 12, scale = 2)
    private BigDecimal diferencia;

    @Column(nullable = false, length = 20)
    private String estado; // ABIERTA, CERRADA

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "observacion_apertura", length = 255)
    private String observacionApertura;

    @Column(name = "observacion_cierre", length = 255)
    private String observacionCierre;

    @PrePersist
    public void prePersist() {
        if (this.fechaApertura == null) {
            this.fechaApertura = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = "ABIERTA";
        }
        if (this.montoIngresos == null) {
            this.montoIngresos = BigDecimal.ZERO;
        }
        if (this.montoEgresos == null) {
            this.montoEgresos = BigDecimal.ZERO;
        }
        if (this.montoEsperado == null) {
            this.montoEsperado = this.montoInicial != null ? this.montoInicial : BigDecimal.ZERO;
        }
    }
}
