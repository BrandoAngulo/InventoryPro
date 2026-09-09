package com.example.soft.inventoryPro.Productos.Controller;

import com.example.soft.inventoryPro.Productos.Service.ProductoService;
import com.example.soft.inventoryPro.Productos.dto.ProductoRequestDTO;
import com.example.soft.inventoryPro.Productos.dto.ProductoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductosController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> registrarProducto(@Valid @RequestBody ProductoRequestDTO dto) {
        ProductoResponseDTO productoGuardado = productoService.registrarProductos(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        List<ProductoResponseDTO> productos = productoService.findByAllProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> findByIdProducto(@PathVariable Long id) {
        ProductoResponseDTO producto = productoService.findByIdProductos(id);
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequestDTO dto) {
        ProductoResponseDTO productoActualizado = productoService.actualizarProductos(id, dto);
        return ResponseEntity.ok(productoActualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ProductoResponseDTO> cambiarEstado(@PathVariable Long id) {
        ProductoResponseDTO productoActualizado = productoService.cambiarEstado(id);
        return ResponseEntity.ok(productoActualizado);
    }
}
