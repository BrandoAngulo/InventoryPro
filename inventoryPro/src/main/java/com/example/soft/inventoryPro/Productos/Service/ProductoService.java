package com.example.soft.inventoryPro.Productos.Service;

import com.example.soft.inventoryPro.Productos.dto.ProductoRequestDTO;
import com.example.soft.inventoryPro.Productos.dto.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {
    ProductoResponseDTO registrarProductos(ProductoRequestDTO dto);
    ProductoResponseDTO actualizarProductos(Long id, ProductoRequestDTO dto);
    void eliminarProductos(Long id);
    ProductoResponseDTO findByIdProductos(Long id);
    List<ProductoResponseDTO> findByAllProductos();
    ProductoResponseDTO cambiarEstado(Long id);
    List<ProductoResponseDTO> listarProductosBajoStock();
}
