package com.example.soft.inventoryPro.Productos.Models;

import com.example.soft.inventoryPro.Bodega.Models.Bodega;
import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProductos;

    @Column(unique = true, nullable = false)
    private String codigo;

    private String nombreProducto;

    private Long precioCompra;

    private Long precioVenta;

    private Long costoPromedio;

    private LocalDate fechaIngreso;

    private Integer cantidad;

    private Integer stockMinimo;

    private Boolean activo;

    private String observacion;

    @Version
    private Long version;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "producto_categoria",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @Builder.Default
    private Set<Categoria> categorias = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "producto_bodega",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "bodega_id")
    )
    @Builder.Default
    private Set<Bodega> Bodega = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "producto_proveedor",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "proveedor_id")
    )
    @Builder.Default
    private Set<Proovedor> proveedores = new HashSet<>();
}
