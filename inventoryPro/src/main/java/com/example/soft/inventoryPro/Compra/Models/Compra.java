package com.example.soft.inventoryPro.Compra.Models;

import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@ToString
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCompra;

    private LocalDate fechaCompra;

    private Double total;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proovedor proovedor;

    @OneToMany(
            mappedBy = "compra",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private Set<DetalleCompra> detalles = new HashSet<>();

}
