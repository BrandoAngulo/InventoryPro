package com.example.soft.inventoryPro.Factura.Repository;

import com.example.soft.inventoryPro.Factura.Models.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {
}
