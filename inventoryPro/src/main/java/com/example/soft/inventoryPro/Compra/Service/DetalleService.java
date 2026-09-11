package com.example.soft.inventoryPro.Compra.Service;

import com.example.soft.inventoryPro.Compra.Models.Compra;

import java.util.List;

public interface DetalleService {
    Compra registrarCompra(Compra compra);
    Compra actualizarCompra(Compra compra);
    String eliminarProovedor(Long id);
    Compra findByIdCompra(Long id);
    List<Compra> findByAllCompra();
}
