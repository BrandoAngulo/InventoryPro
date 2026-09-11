package com.example.soft.inventoryPro.Pedido.Models;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalles_pedido")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Productos producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_lista", nullable = false)
    private Long precioLista;

    @Column(name = "precio_ajustado", nullable = false)
    private Long precioAjustado;

    @Column(name = "descuento_unitario", nullable = false)
    private Long descuentoUnitario;

    @Column(name = "subtotal_renglon", nullable = false)
    private Long subtotalRenglon;
}
