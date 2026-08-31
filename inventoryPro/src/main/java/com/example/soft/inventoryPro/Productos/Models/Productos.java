package com.example.soft.inventoryPro.Productos.Models;

import com.example.soft.inventoryPro.Bodega.Models.Bodega;
import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProductos;


    private String nombreProducto;

    private Integer precioCompra;

    private Integer precioVenta;
    private LocalDate fechaIngreso;
    private Integer cantidad;

    private String observacion;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "producto_categoria",
            joinColumns = @JoinColumn(
                    name = "producto_id"
            ), inverseJoinColumns = @JoinColumn(name = "categoria_id")


    )
    Set<Categoria> categorias = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "producto_bodega",
            joinColumns = @JoinColumn(
                    name = "producto_id"
            ), inverseJoinColumns = @JoinColumn(name = "bodega_id")


    )
    Set<Bodega> Bodega = new HashSet<>();
     }

