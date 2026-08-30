package com.example.soft.inventoryPro.Bodega.Repository;

import com.example.soft.inventoryPro.Bodega.Models.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Integer> {
}
