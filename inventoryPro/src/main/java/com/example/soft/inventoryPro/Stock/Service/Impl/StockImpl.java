package com.example.soft.inventoryPro.Stock.Service.Impl;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Stock.Models.Stock;
import com.example.soft.inventoryPro.Stock.Repository.StockRepository;
import com.example.soft.inventoryPro.Stock.Service.StockService;
import com.example.soft.inventoryPro.Stock.dto.StockRequestDTO;
import com.example.soft.inventoryPro.Stock.dto.StockResponseDTO;
import com.example.soft.inventoryPro.Stock.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockImpl implements StockService {

    private final StockRepository stockRepository;
    private final ProductoRepository productosRepository;

    @Override
    public StockResponseDTO registrarStockProducto(StockRequestDTO dto) {
        Productos productos = productosRepository.findById(dto.getIdProducto().intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + dto.getIdProducto()));

        Optional<Stock> stockExistente = stockRepository.findByProductos_IdProductos(productos.getIdProductos());
        if (stockExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un stock registrado para el producto con ID: " + productos.getIdProductos());
        }

        Stock stock = Stock.builder()
                .cantidadStock(dto.getCantidadStock())
                .productos(productos)
                .build();

        Stock guardado = stockRepository.save(stock);
        return StockMapper.toDTO(guardado);
    }

    @Override
    public StockResponseDTO actualizarStock(Integer id, StockRequestDTO dto) {
        Stock stockExistente = stockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock no encontrado con ID: " + id));

        if (dto.getIdProducto() != null) {
            Productos productos = productosRepository.findById(dto.getIdProducto().intValue())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + dto.getIdProducto()));
            stockExistente.setProductos(productos);
        }

        stockExistente.setCantidadStock(dto.getCantidadStock());
        Stock actualizado = stockRepository.save(stockExistente);
        return StockMapper.toDTO(actualizado);
    }

    @Override
    public StockResponseDTO finByIdStock(Integer idStock) {
        Stock stock = stockRepository.findById(idStock)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock no encontrado con ID: " + idStock));
        return StockMapper.toDTO(stock);
    }

    @Override
    public List<StockResponseDTO> findByAllStock() {
        List<Stock> stockList = stockRepository.findAll();
        if (stockList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La lista está vacía");
        }
        return stockList.stream().map(StockMapper::toDTO).collect(Collectors.toList());
    }
}
