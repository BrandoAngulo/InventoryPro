package com.example.soft.inventoryPro.Factura.Repository;

import com.example.soft.inventoryPro.Factura.Models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByNumeroFactura(String numeroFactura);

    @Query("SELECT SUM(f.total) FROM Factura f WHERE f.fecha BETWEEN :inicio AND :fin AND f.estado = :estado")
    Long sumTotalByFechaBetweenAndEstado(@Param("inicio") LocalDateTime inicio,
                                         @Param("fin") LocalDateTime fin,
                                         @Param("estado") String estado);

    List<Factura> findTop5ByOrderByFechaDesc();

    @Query("SELECT DISTINCT f FROM Factura f JOIN FETCH f.cliente LEFT JOIN FETCH f.detalles d LEFT JOIN FETCH d.producto WHERE f.id = :id")
    Optional<Factura> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT f FROM Factura f JOIN FETCH f.cliente LEFT JOIN FETCH f.detalles d LEFT JOIN FETCH d.producto ORDER BY f.fecha DESC")
    List<Factura> findAllWithDetails();
}
