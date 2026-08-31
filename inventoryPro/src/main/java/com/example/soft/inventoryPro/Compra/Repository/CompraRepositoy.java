package com.example.soft.inventoryPro.Compra.Repository;

import com.example.soft.inventoryPro.Compra.Models.Compra;
import com.example.soft.inventoryPro.Compra.Models.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepositoy extends JpaRepository<Compra, Integer> {
}
