package com.example.soft.inventoryPro.Caja.Repository;

import com.example.soft.inventoryPro.Caja.Models.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
    Optional<Caja> findByEstado(String estado);
    Optional<Caja> findFirstByEstadoOrderByFechaAperturaDesc(String estado);
}
