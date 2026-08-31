package com.example.soft.inventoryPro.Productos.Service;

import com.example.soft.inventoryPro.Productos.Models.ProductoResponse;
import com.example.soft.inventoryPro.Productos.Models.Productos;

import java.util.List;

public interface ProductoService {
    Productos registrarProductos(Productos productos);

    Productos actualizarProductos(Productos productos);

    String eliminarProductos(Integer id);

    Productos findByIdProductos(Integer id);

    List<ProductoResponse> findByAllProductos();
}
