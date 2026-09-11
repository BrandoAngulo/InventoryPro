package com.example.soft.inventoryPro.Compra.Controller;

import com.example.soft.inventoryPro.Compra.Service.CompraService;
import com.example.soft.inventoryPro.Compra.dto.CompraRequestDTO;
import com.example.soft.inventoryPro.Compra.dto.CompraResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    @PostMapping
    public ResponseEntity<CompraResponseDTO> registrarCompra(@Valid @RequestBody CompraRequestDTO dto) {
        CompraResponseDTO compraGuardada = compraService.registrarCompra(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(compraGuardada);
    }

    @GetMapping
    public ResponseEntity<List<CompraResponseDTO>> listarCompras() {
        List<CompraResponseDTO> compras = compraService.findByAllCompra();
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> findByIdCompra(@PathVariable Long id) {
        CompraResponseDTO compra = compraService.findByIdCompra(id);
        return ResponseEntity.ok(compra);
    }
}
