package com.example.soft.inventoryPro.Stock.Service;

import com.example.soft.inventoryPro.Stock.dto.StockRequestDTO;
import com.example.soft.inventoryPro.Stock.dto.StockResponseDTO;

import java.util.List;

public interface StockService {
    StockResponseDTO registrarStockProducto(StockRequestDTO dto);
    StockResponseDTO actualizarStock(Integer id, StockRequestDTO dto);
    StockResponseDTO finByIdStock(Integer idStock);
    List<StockResponseDTO> findByAllStock();
}
