package com.example.soft.inventoryPro.Proovedor.Service;

import com.example.soft.inventoryPro.Productos.Models.ProductoResponse;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;

import java.util.List;

public interface ProovedorService {
    Proovedor registrarProovedor(Proovedor proovedor);

    Proovedor actualizarProovedor(Proovedor proovedor);

    String eliminarProovedor(Integer id);

    Proovedor findByIdProovedor(Integer id);

    List<Proovedor> findByAllProovedor();
}
