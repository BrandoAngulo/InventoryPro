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
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStock;

    private Integer cantidadStock;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productos_id")
    private Productos productos;
}
