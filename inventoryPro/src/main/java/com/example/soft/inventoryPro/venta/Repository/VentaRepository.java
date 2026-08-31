package com.example.soft.inventoryPro.venta.Repository;

import com.example.soft.inventoryPro.venta.Models.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Ventas,Integer> {
}
