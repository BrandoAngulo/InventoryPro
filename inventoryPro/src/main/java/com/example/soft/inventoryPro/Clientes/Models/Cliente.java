package com.example.soft.inventoryPro.Clientes.Models;

import com.example.soft.inventoryPro.Usuario.Models.Usuario;
import com.example.soft.inventoryPro.venta.Models.Ventas;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idCliente;


    Integer documento;

    String nombre;


    String apellido;


    String direccion;


    String correo;


    String celular;

    String estado;

    @Transient
    @OneToMany(mappedBy = "clientes", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ventas> ventas;



    @ManyToOne
    @JoinTable(name = "cliente_usuario",
            joinColumns = @JoinColumn(
                    name = "cliente_id"
            ), inverseJoinColumns = @JoinColumn(name = "usuario_id")


    )
    private Usuario usuario;


}
