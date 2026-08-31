package com.example.soft.inventoryPro.Compra.Models;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalleCompra;

    private Integer cantidad;

    private Double precioCompra;

    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "id_compra")
    @JsonBackReference
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Productos producto;
}
