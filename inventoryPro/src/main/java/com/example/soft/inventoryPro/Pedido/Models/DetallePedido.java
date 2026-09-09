package com.example.soft.inventoryPro.Pedido.Models;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_pedido")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    @ToString.Exclude
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Productos producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_lista", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioLista;

    @Column(name = "precio_ajustado", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioAjustado;

    @Column(name = "descuento_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal descuentoUnitario;

    @Column(name = "subtotal_renglon", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotalRenglon;
}
