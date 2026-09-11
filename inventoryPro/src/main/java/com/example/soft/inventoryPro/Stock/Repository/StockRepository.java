package com.example.soft.inventoryPro.Stock.Repository;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Stock.Models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByProductosIn(Set<Productos> producto);
    Optional<Stock> findByProductos_IdProductos(Long idProductos);
}
