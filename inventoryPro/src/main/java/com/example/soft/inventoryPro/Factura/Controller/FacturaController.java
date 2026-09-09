package com.example.soft.inventoryPro.Factura.Controller;

import com.example.soft.inventoryPro.Factura.Service.FacturaService;
import com.example.soft.inventoryPro.venta.dto.FacturaRequestDTO;
import com.example.soft.inventoryPro.venta.dto.FacturaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping
    public ResponseEntity<FacturaResponseDTO> registrarFactura(@Valid @RequestBody FacturaRequestDTO dto) {
        FacturaResponseDTO factura = facturaService.registrarFactura(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(factura);
    }

    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> listarFacturas() {
        return ResponseEntity.ok(facturaService.obtenerFacturas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> obtenerFacturaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.obtenerFacturaPorId(id));
    }
}
