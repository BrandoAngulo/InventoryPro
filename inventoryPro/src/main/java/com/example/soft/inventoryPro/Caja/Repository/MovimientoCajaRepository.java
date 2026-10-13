package com.example.soft.inventoryPro.Caja.Repository;

import com.example.soft.inventoryPro.Caja.Models.MovimientoCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {
    List<MovimientoCaja> findByCaja_IdOrderByFechaDesc(Long cajaId);

    @Query("SELECT DISTINCT m FROM MovimientoCaja m JOIN FETCH m.caja WHERE m.caja.id = :cajaId ORDER BY m.fecha DESC")
    List<MovimientoCaja> findByCajaIdWithCaja(@Param("cajaId") Long cajaId);

    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM MovimientoCaja m WHERE m.caja.id = :cajaId AND m.tipo = :tipo")
    Long sumMontoByCajaIdAndTipo(@Param("cajaId") Long cajaId, @Param("tipo") String tipo);
}
