package com.example.soft.inventoryPro.Inventario.Controller;

import com.example.soft.inventoryPro.Inventario.Models.TipoAjuste;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import com.example.soft.inventoryPro.Inventario.Service.InventarioService;
import com.example.soft.inventoryPro.Inventario.dto.*;
import com.example.soft.inventoryPro.Productos.dto.ProductoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoInventarioResponseDTO>> getMovimientos(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) TipoMovimiento tipo) {
        return ResponseEntity.ok(inventarioService.getMovimientos(productoId, tipo));
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoResponseDTO>> getProductos(
            @RequestParam(required = false) Boolean bajoStock) {
        return ResponseEntity.ok(inventarioService.getProductos(bajoStock));
    }

    @PostMapping("/registro-inicial")
    public ResponseEntity<List<RegistroInicialResponseDTO>> registrarStockInicial(
            @Valid @RequestBody RegistroInicialRequestDTO request) {
        List<RegistroInicialResponseDTO> response = inventarioService.registrarStockInicial(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/ajustes")
    public ResponseEntity<AjusteStockResponseDTO> registrarAjusteStock(
            @Valid @RequestBody AjusteStockRequestDTO request) {
        AjusteStockResponseDTO response = inventarioService.registrarAjusteStock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/ajustes")
    public ResponseEntity<List<AjusteStockResponseDTO>> getAjustes(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) TipoAjuste tipo) {
        return ResponseEntity.ok(inventarioService.getAjustes(productoId, tipo));
    }
}
