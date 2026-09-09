package com.example.soft.inventoryPro.Compra.Models;

import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = "detalles")
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Integer idCompra;

    @Column(name = "numero_factura_proveedor", length = 50)
    private String numeroFacturaProveedor;

    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    @Column(length = 255)
    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private Proovedor proovedor;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<DetalleCompra> detalles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.fechaCompra == null) {
            this.fechaCompra = LocalDateTime.now();
        }
    }

    public Long getId() {
        return idCompra != null ? idCompra.longValue() : null;
    }
}
