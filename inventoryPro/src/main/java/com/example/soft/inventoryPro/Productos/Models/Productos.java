package com.example.soft.inventoryPro.Productos.Models;

import com.example.soft.inventoryPro.Bodega.Models.Bodega;
import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private String codigo;

    private String nombreProducto;

    private Integer precioCompra;

    private Integer precioVenta;

    private BigDecimal costoPromedio;

    private LocalDate fechaIngreso;

    private Integer cantidad;

    private Integer stockMinimo;

    private Boolean activo;

    private String observacion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "producto_categoria",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @Builder.Default
    Set<Categoria> categorias = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "producto_bodega",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "bodega_id")
    )
    @Builder.Default
    Set<Bodega> Bodega = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "producto_proveedor",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "proveedor_id")
    )
    @Builder.Default
    Set<Proovedor> proveedores = new HashSet<>();
}
