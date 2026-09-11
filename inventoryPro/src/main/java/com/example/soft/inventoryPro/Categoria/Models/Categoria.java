package com.example.soft.inventoryPro.Categoria.Models;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    private String nombreCategoria;

    private String descripcion;

    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private Set<Productos> productos = new HashSet<>();

    public String getNombre() {
        return nombreCategoria;
    }
}
