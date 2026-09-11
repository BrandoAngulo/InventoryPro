package com.example.soft.inventoryPro.Stock.Controller;

import com.example.soft.inventoryPro.Stock.Service.StockService;
import com.example.soft.inventoryPro.Stock.dto.StockRequestDTO;
import com.example.soft.inventoryPro.Stock.dto.StockResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<StockResponseDTO> registrarStock(@Valid @RequestBody StockRequestDTO dto) {
        StockResponseDTO stockGuardado = stockService.registrarStockProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(stockGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockResponseDTO> actualizarStock(@PathVariable Long id, @Valid @RequestBody StockRequestDTO dto) {
        StockResponseDTO stockActualizado = stockService.actualizarStock(id, dto);
        return ResponseEntity.ok(stockActualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDTO> findByIdStock(@PathVariable Long id) {
        StockResponseDTO stock = stockService.finByIdStock(id);
        return ResponseEntity.ok(stock);
    }

    @GetMapping
    public ResponseEntity<List<StockResponseDTO>> traerStock() {
        List<StockResponseDTO> stocks = stockService.findByAllStock();
        return ResponseEntity.ok(stocks);
    }
}
