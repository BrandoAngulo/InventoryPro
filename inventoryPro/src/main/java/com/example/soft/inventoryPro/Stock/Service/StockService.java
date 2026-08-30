package com.example.soft.inventoryPro.Stock.Service;

import com.example.soft.inventoryPro.Stock.Models.Stock;

import java.util.List;

public interface StockService {
    Stock registrarStockProducto(Stock stock);

    Stock actualizarStock(Stock stock);

    Stock finByIdStock(Integer idStock);

    List<Stock> findByAllStock();
}
