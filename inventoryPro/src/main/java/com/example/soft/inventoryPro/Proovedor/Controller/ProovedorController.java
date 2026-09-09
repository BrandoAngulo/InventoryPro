package com.example.soft.inventoryPro.Proovedor.Controller;

import com.example.soft.inventoryPro.Proovedor.Service.ProovedorService;
import com.example.soft.inventoryPro.Proovedor.dto.ProveedorRequestDTO;
import com.example.soft.inventoryPro.Proovedor.dto.ProveedorResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProovedorController {

    private final ProovedorService proovedorService;

    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> registrarProovedor(@Valid @RequestBody ProveedorRequestDTO dto) {
        ProveedorResponseDTO proveedorGuardado = proovedorService.registrarProovedor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorGuardado);
    }

    @GetMapping
    public ResponseEntity<List<ProveedorResponseDTO>> listarProveedores() {
        List<ProveedorResponseDTO> proveedores = proovedorService.findByAllProovedor();
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> proveedorPorId(@PathVariable Long id) {
        ProveedorResponseDTO proveedor = proovedorService.findByIdProovedor(id);
        return ResponseEntity.ok(proveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> actualizarProveedor(@PathVariable Long id, @Valid @RequestBody ProveedorRequestDTO dto) {
        ProveedorResponseDTO proveedorActualizado = proovedorService.actualizarProovedor(id, dto);
        return ResponseEntity.ok(proveedorActualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ProveedorResponseDTO> cambiarEstado(@PathVariable Long id) {
        ProveedorResponseDTO proveedorActualizado = proovedorService.cambiarEstado(id);
        return ResponseEntity.ok(proveedorActualizado);
    }
}
