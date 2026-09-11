package com.example.soft.inventoryPro.Compra.Repository;

import com.example.soft.inventoryPro.Compra.Models.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    @Query("SELECT c FROM Compra c ORDER BY c.fechaCompra DESC")
    List<Compra> findTop5ByOrderByFechaCompraDesc();

    @Query("SELECT DISTINCT c FROM Compra c LEFT JOIN FETCH c.proovedor LEFT JOIN FETCH c.detalles d LEFT JOIN FETCH d.producto WHERE c.idCompra = :id")
    Optional<Compra> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT c FROM Compra c LEFT JOIN FETCH c.proovedor LEFT JOIN FETCH c.detalles d LEFT JOIN FETCH d.producto ORDER BY c.fechaCompra DESC")
    List<Compra> findAllWithDetails();
}
