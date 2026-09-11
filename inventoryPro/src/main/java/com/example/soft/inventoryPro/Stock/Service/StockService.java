package com.example.soft.inventoryPro.Stock.Service;

import com.example.soft.inventoryPro.Stock.dto.StockRequestDTO;
import com.example.soft.inventoryPro.Stock.dto.StockResponseDTO;

import java.util.List;

public interface StockService {
    StockResponseDTO registrarStockProducto(StockRequestDTO dto);
    StockResponseDTO actualizarStock(Long id, StockRequestDTO dto);
    StockResponseDTO finByIdStock(Long idStock);
    List<StockResponseDTO> findByAllStock();
}
