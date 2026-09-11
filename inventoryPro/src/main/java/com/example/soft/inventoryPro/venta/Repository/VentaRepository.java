package com.example.soft.inventoryPro.venta.Repository;

import com.example.soft.inventoryPro.venta.Models.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Ventas, Long> {
    @Query("SELECT DISTINCT v FROM Ventas v LEFT JOIN FETCH v.clientes LEFT JOIN FETCH v.usuario LEFT JOIN FETCH v.detalles d LEFT JOIN FETCH d.productos WHERE v.id = :id")
    Optional<Ventas> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT v FROM Ventas v LEFT JOIN FETCH v.clientes LEFT JOIN FETCH v.usuario LEFT JOIN FETCH v.detalles d LEFT JOIN FETCH d.productos ORDER BY v.fecha DESC")
    List<Ventas> findAllWithDetails();
}
