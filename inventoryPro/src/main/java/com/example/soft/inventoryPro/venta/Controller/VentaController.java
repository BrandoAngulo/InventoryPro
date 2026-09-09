package com.example.soft.inventoryPro.venta.Controller;

import com.example.soft.inventoryPro.venta.Service.VentaService;
import com.example.soft.inventoryPro.venta.dto.VentaRequestDTO;
import com.example.soft.inventoryPro.venta.dto.VentaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaResponseDTO> registrarVenta(@Valid @RequestBody VentaRequestDTO dto) {
        VentaResponseDTO vs = ventaService.registrarVentas(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vs);
    }

    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> listarTodasVentas() {
        List<VentaResponseDTO> vs = ventaService.listarTodasVentas();
        return ResponseEntity.ok(vs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> listarVentaPorId(@PathVariable Integer id) {
        VentaResponseDTO vs = ventaService.listarVentas(id);
        return ResponseEntity.ok(vs);
    }
}
