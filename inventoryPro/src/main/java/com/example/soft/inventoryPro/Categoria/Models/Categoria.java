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
@ToString
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idCategoria;


    String nombreCategoria;

    @ManyToMany(mappedBy = "categorias")
    @JsonIgnore
    private Set<Productos> productos = new HashSet<>();
}
