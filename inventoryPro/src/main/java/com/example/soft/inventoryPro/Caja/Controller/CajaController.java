package com.example.soft.inventoryPro.Caja.Controller;

import com.example.soft.inventoryPro.Caja.Service.CajaService;
import com.example.soft.inventoryPro.Caja.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/caja")
@RequiredArgsConstructor
public class CajaController {

    private final CajaService cajaService;

    @PostMapping("/aperturas")
    public ResponseEntity<CajaResponseDTO> abrirCaja(@Valid @RequestBody AperturaCajaRequestDTO dto) {
        CajaResponseDTO caja = cajaService.abrirCaja(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(caja);
    }

    @GetMapping("/actual")
    public ResponseEntity<CajaResponseDTO> getCajaActual() {
        return ResponseEntity.ok(cajaService.getCajaActual());
    }

    @PostMapping("/movimientos")
    public ResponseEntity<MovimientoCajaResponseDTO> registrarMovimiento(@Valid @RequestBody MovimientoCajaRequestDTO dto) {
        MovimientoCajaResponseDTO movimiento = cajaService.registrarMovimiento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }

    @PostMapping("/cierres")
    public ResponseEntity<CajaResponseDTO> cerrarCaja(@Valid @RequestBody CierreCajaRequestDTO dto) {
        CajaResponseDTO caja = cajaService.cerrarCaja(dto);
        return ResponseEntity.ok(caja);
    }

    @GetMapping("/{id}/movimientos")
    public ResponseEntity<List<MovimientoCajaResponseDTO>> getMovimientosCaja(@PathVariable Long id) {
        return ResponseEntity.ok(cajaService.getMovimientosCaja(id));
    }
}
