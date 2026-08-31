package com.example.soft.inventoryPro.venta.Models;

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
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "venta_id")
    private Ventas ventas;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Productos productos;

    private Integer cantidad;

    private Integer precioUnitario;
}
