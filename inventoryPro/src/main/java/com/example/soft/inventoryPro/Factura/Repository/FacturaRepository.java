package com.example.soft.inventoryPro.Factura.Repository;

import com.example.soft.inventoryPro.Factura.Models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByNumeroFactura(String numeroFactura);

    @Query("SELECT SUM(f.total) FROM Factura f WHERE f.fecha BETWEEN :inicio AND :fin AND f.estado = :estado")
    BigDecimal sumTotalByFechaBetweenAndEstado(@Param("inicio") LocalDateTime inicio,
                                               @Param("fin") LocalDateTime fin,
                                               @Param("estado") String estado);

    List<Factura> findTop5ByOrderByFechaDesc();
}
