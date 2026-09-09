package com.example.soft.inventoryPro.Productos.Repository;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Productos, Integer> {
    Optional<Productos> findByIdProductos(Integer idProductos);

    @Query("SELECT p FROM Productos p WHERE p.cantidad <= p.stockMinimo AND (p.activo = true OR p.activo IS NULL)")
    List<Productos> findProductosBajoStock();
}
