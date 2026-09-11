package com.example.soft.inventoryPro.Proovedor.Models;

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
public class Proovedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProovedor;

    private String nombreProovedor;

    private String documento;

    private String telefono;

    private String correo;

    private String direccion;

    private String ciudad;

    private Boolean estado;

    @ManyToMany(mappedBy = "proveedores", fetch = FetchType.LAZY)
    @Transient
    private Set<Productos> productos;
}
