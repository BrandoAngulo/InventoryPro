package com.example.soft.inventoryPro.Caja.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cajas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Caja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monto_inicial", nullable = false)
    private Long montoInicial;

    @Column(name = "monto_ingresos", nullable = false)
    private Long montoIngresos;

    @Column(name = "monto_egresos", nullable = false)
    private Long montoEgresos;

    @Column(name = "monto_esperado", nullable = false)
    private Long montoEsperado;

    @Column(name = "monto_cierre")
    private Long montoCierre;

    private Long diferencia;

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

    @Version
    private Long version;

    @PrePersist
    public void prePersist() {
        if (this.fechaApertura == null) {
            this.fechaApertura = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = "ABIERTA";
        }
        if (this.montoIngresos == null) {
            this.montoIngresos = 0L;
        }
        if (this.montoEgresos == null) {
            this.montoEgresos = 0L;
        }
        if (this.montoEsperado == null) {
            this.montoEsperado = this.montoInicial != null ? this.montoInicial : 0L;
        }
    }
}
