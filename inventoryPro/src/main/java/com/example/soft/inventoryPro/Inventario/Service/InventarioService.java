package com.example.soft.inventoryPro.Inventario.Service;

import com.example.soft.inventoryPro.Inventario.Models.TipoAjuste;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import com.example.soft.inventoryPro.Inventario.dto.*;
import com.example.soft.inventoryPro.Productos.dto.ProductoResponseDTO;

import java.util.List;

public interface InventarioService {
    List<MovimientoInventarioResponseDTO> getMovimientos(Long productoId, TipoMovimiento tipo);
    List<ProductoResponseDTO> getProductos(Boolean bajoStock);
    List<RegistroInicialResponseDTO> registrarStockInicial(RegistroInicialRequestDTO request);
    AjusteStockResponseDTO registrarAjusteStock(AjusteStockRequestDTO request);
    List<AjusteStockResponseDTO> getAjustes(Long productoId, TipoAjuste tipo);
}
