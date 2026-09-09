package com.example.soft.inventoryPro.Stock.mapper;

import com.example.soft.inventoryPro.Stock.Models.Stock;
import com.example.soft.inventoryPro.Stock.dto.StockResponseDTO;

public class StockMapper {

    public static StockResponseDTO toDTO(Stock entity) {
        if (entity == null) return null;
        return StockResponseDTO.builder()
                .idStock(entity.getIdStock())
                .idProducto(entity.getProductos() != null && entity.getProductos().getIdProductos() != null ? entity.getProductos().getIdProductos().longValue() : null)
                .nombreProducto(entity.getProductos() != null ? entity.getProductos().getNombreProducto() : null)
                .cantidadStock(entity.getCantidadStock())
                .build();
    }
}
