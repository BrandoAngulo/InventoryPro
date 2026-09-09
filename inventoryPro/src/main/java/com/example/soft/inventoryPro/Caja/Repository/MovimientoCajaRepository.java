package com.example.soft.inventoryPro.Caja.Repository;

import com.example.soft.inventoryPro.Caja.Models.MovimientoCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {
    List<MovimientoCaja> findByCaja_Id(Long cajaId);
}
