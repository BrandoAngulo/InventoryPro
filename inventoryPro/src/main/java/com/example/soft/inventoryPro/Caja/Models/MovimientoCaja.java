package com.example.soft.inventoryPro.Caja.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_caja")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caja_id", nullable = false)
    private Caja caja;

    @Column(nullable = false, length = 20)
    private String tipo; // INGRESO_VENTA, INGRESO_MANUAL, EGRESO_MANUAL

    @Column(nullable = false)
    private Long monto;

    @Column(nullable = false, length = 255)
    private String concepto;

    @Column(name = "factura_id")
    private Long facturaId;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }
}
