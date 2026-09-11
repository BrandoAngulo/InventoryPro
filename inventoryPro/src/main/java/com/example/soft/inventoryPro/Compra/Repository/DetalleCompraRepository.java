package com.example.soft.inventoryPro.Compra.Repository;

import com.example.soft.inventoryPro.Compra.Models.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
}
