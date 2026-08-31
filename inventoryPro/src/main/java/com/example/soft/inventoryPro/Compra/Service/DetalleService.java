package com.example.soft.inventoryPro.Compra.Service;

import com.example.soft.inventoryPro.Compra.Models.Compra;
import com.example.soft.inventoryPro.Compra.Models.DetalleCompra;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;

import java.util.List;

public interface DetalleService {
    Compra registrarCompra(Compra compra);

    Compra actualizarCompra(Compra compra);

    String eliminarProovedor(Integer id);

    Compra findByIdCompra(Integer id);

    List<Compra> findByAllCompra();
}
