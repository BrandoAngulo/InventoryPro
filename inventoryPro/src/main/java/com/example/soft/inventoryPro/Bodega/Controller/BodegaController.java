package com.example.soft.inventoryPro.Bodega.Controller;

import com.example.soft.inventoryPro.Bodega.Service.BodegaService;
import com.example.soft.inventoryPro.Bodega.dto.BodegaRequestDTO;
import com.example.soft.inventoryPro.Bodega.dto.BodegaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService bodegaService;

    @PostMapping
    public ResponseEntity<BodegaResponseDTO> registrarBodega(@Valid @RequestBody BodegaRequestDTO dto) {
        BodegaResponseDTO bodegaRegistrada = bodegaService.registrarBodega(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bodegaRegistrada);
    }

    @GetMapping
    public ResponseEntity<List<BodegaResponseDTO>> listarBodegas() {
        List<BodegaResponseDTO> bodegas = bodegaService.traerTodasBoderga();
        return ResponseEntity.ok(bodegas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponseDTO> listarBodega(@PathVariable Long id) {
        BodegaResponseDTO bodega = bodegaService.traerIdBoderga(id);
        return ResponseEntity.ok(bodega);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BodegaResponseDTO> actualizarBodega(@PathVariable Long id, @Valid @RequestBody BodegaRequestDTO dto) {
        BodegaResponseDTO bodegaActualizada = bodegaService.actualizarBodega(id, dto);
        return ResponseEntity.ok(bodegaActualizada);
    }
}
