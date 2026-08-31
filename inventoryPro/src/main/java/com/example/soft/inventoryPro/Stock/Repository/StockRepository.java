package com.example.soft.inventoryPro.Stock.Repository;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Stock.Models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StockRepository  extends JpaRepository<Stock,Integer> {

    List<Stock> findByProductosIn(Set<Productos> producto);
    //findByProductos
    Optional<Stock> findByProductos_IdProductos(Integer idProductos);
}
