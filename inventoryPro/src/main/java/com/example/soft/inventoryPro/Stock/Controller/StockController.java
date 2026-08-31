package com.example.soft.inventoryPro.Stock.Controller;

import com.example.soft.inventoryPro.Stock.Models.Stock;
import com.example.soft.inventoryPro.Stock.Service.Impl.StockImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    private StockImpl stockImpl;

    @PostMapping("/registrarStock")
    public ResponseEntity<Object> registrarStock(@RequestBody Stock stock) {

        Stock stockGuardado = stockImpl.registrarStockProducto(stock);

        return ResponseEntity.status(HttpStatus.CREATED).body(stockGuardado);

    }

    @PutMapping("/actualizar")
    public ResponseEntity<Object> stockStock(@RequestBody Stock stock) {

        Stock StockActualizado = stockImpl.actualizarStock(stock);

        return ResponseEntity.status(HttpStatus.CREATED).body(StockActualizado);

    }

    @GetMapping("/traerporId/{id}")
    public ResponseEntity<Object> findByIdstock(@PathVariable Integer id) {
        Stock stockFindById = stockImpl.finByIdStock(id);
        return ResponseEntity.status(HttpStatus.OK).body(stockFindById);

    }

    @GetMapping("/traerporAll")
    public ResponseEntity<Object> traerStock() {
        List<Stock> stocksFindByAll = stockImpl.findByAllStock();
        return ResponseEntity.status(HttpStatus.OK).body(stocksFindByAll);

    }
}
