package com.example.soft.inventoryPro.venta.Models;

import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Usuario.Models.Usuario;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Ventas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private Long total;

    @OneToMany(mappedBy = "ventas", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DetalleVenta> detalles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "producto_pedidos",
            joinColumns = @JoinColumn(
                    name = "producto_id"
            ), inverseJoinColumns = @JoinColumn(name = "venta_id")
    )
    @Builder.Default
    private Set<Productos> productos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "pedido_cliente",
            joinColumns = @JoinColumn(
                    name = "pedido_id"
            ), inverseJoinColumns = @JoinColumn(name = "cliente_id")
    )
    private Cliente clientes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "pedido_usuario",
            joinColumns = @JoinColumn(
                    name = "pedido_id"
            ), inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Usuario usuario;
}
