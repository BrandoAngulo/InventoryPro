package com.example.soft.inventoryPro.Compra.Repository;

import com.example.soft.inventoryPro.Compra.Models.DetalleCompra;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {
}
