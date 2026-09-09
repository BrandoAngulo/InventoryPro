package com.example.soft.inventoryPro.Categoria.Controller;

import com.example.soft.inventoryPro.Categoria.Service.CategoriaService;
import com.example.soft.inventoryPro.Categoria.dto.CategoriaRequestDTO;
import com.example.soft.inventoryPro.Categoria.dto.CategoriaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> registrarCategoria(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO categoriaGuardada = categoriaService.registrarCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaGuardada);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        List<CategoriaResponseDTO> categorias = categoriaService.traerTodoCategoria();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> findByIdCategoria(@PathVariable Long id) {
        CategoriaResponseDTO categoria = categoriaService.findByIdCategoria(id);
        return ResponseEntity.ok(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO categoriaActualizada = categoriaService.actualizarCategoria(id, dto);
        return ResponseEntity.ok(categoriaActualizada);
    }
}
