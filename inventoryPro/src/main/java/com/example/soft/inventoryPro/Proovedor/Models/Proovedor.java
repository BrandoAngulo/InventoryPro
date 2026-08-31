package com.example.soft.inventoryPro.Proovedor.Models;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class Proovedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProovedor;


    private String nombreProovedor;

    private String documento;

    private String telefono;

    private String correo;

    private String direccion;

    private String ciudad;

    private Boolean estado;

    @ManyToMany(mappedBy = "proveedores")
    private Set<Productos> productos;

}
