package com.example.soft.inventoryPro.Factura.Models;

import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_factura", nullable = false, unique = true, length = 50)
    private String numeroFactura;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private Long subtotal;

    @Column(nullable = false)
    private Long impuesto;

    @Column(nullable = false)
    private Long total;

    @Column(name = "forma_pago", nullable = false, length = 20)
    private String formaPago;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 255)
    private String observacion;

    @Version
    private Long version;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleFactura> detalles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = "PAGADA";
        }
        if (this.impuesto == null) {
            this.impuesto = 0L;
        }
        if (this.subtotal == null) {
            this.subtotal = 0L;
        }
        if (this.total == null) {
            this.total = 0L;
        }
    }
}
