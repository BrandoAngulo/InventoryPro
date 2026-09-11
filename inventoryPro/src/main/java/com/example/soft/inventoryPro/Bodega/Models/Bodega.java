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
public class Bodega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBodega;

    private String nombre;

    private String fechaCreacionBodega;

    @Transient
    @ManyToMany(mappedBy = "Bodega", fetch = FetchType.LAZY)
    private Set<Productos> productosSet;
}
