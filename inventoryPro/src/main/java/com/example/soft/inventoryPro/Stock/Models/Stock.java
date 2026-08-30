package com.example.soft.inventoryPro.Stock.Models;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idStock;

    Integer cantidadStock;

    @OneToOne
    @JoinColumn(name = "productos_id")
    private Productos productos;
}
