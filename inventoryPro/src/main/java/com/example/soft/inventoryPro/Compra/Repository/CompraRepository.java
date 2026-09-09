package com.example.soft.inventoryPro.Compra.Repository;

import com.example.soft.inventoryPro.Compra.Models.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {
    @Query("SELECT c FROM Compra c ORDER BY c.fechaCompra DESC")
    List<Compra> findTop5ByOrderByFechaCompraDesc();
}
