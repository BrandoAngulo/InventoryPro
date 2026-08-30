package com.example.soft.inventoryPro.Bodega.Models;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class Bodega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idBodega;


    String nombre;

    String fechaCreacionBodega;


    @Transient
    @ManyToMany(mappedBy = "Bodega")
    private Set<Productos> productosSet;
}
