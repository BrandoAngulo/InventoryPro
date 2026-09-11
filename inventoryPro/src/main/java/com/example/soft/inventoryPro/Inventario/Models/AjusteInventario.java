package com.example.soft.inventoryPro.Inventario.Models;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ajustes_inventario")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AjusteInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Productos producto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_ajuste", nullable = false, length = 20)
    private TipoAjuste tipoAjuste;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "costo_unitario_ingreso")
    private Long costoUnitarioIngreso;

    @Column(name = "costo_promedio_anterior", nullable = false)
    private Long costoPromedioAnterior;

    @Column(name = "costo_promedio_resultante", nullable = false)
    private Long costoPromedioResultante;

    @Column(name = "stock_anterior", nullable = false)
    private Integer stockAnterior;

    @Column(name = "stock_resultante", nullable = false)
    private Integer stockResultante;

    @Column(nullable = false, length = 255)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }
}
