package com.example.soft.inventoryPro.Stock.Service.Impl;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Stock.Models.Stock;
import com.example.soft.inventoryPro.Stock.Repository.StockRepository;
import com.example.soft.inventoryPro.Stock.Service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StockImpl implements StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductoRepository productosRepository;
    @Override
    public Stock registrarStockProducto(Stock stock) {
        Productos productos = productosRepository.findById(stock.getProductos().getIdProductos()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Categoría no encontrada con ID: " ));

        Optional<Stock> stockExistente = stockRepository.findByProductos_IdProductos(productos.getIdProductos());
        if (stockExistente.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un stock registrado para el producto con ID: " + productos.getIdProductos()
            );
        }
        try{



            stock.setProductos( productos);



            return stockRepository.save(stock);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stock actualizarStock(Stock stock) {
        Productos productos = productosRepository.findById(stock.getProductos().getIdProductos()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Categoría no encontrada con ID: " ));


        try{

            stock.setProductos( productos);

            return stockRepository.save(stock);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stock finByIdStock(Integer idStock) {
        try{
            return stockRepository.findById(idStock).orElseThrow();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Stock> findByAllStock() {
        List<Stock> stockList = stockRepository.findAll();

        try{
            return stockList;
        }catch (Exception e) {
            throw new RuntimeException(e);
        } }

    }
