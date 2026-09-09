package com.example.soft.inventoryPro.Categoria.Service;

import com.example.soft.inventoryPro.Categoria.dto.CategoriaRequestDTO;
import com.example.soft.inventoryPro.Categoria.dto.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {
    CategoriaResponseDTO registrarCategoria(CategoriaRequestDTO dto);
    CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO dto);
    void eliminarCategoria(Long id);
    CategoriaResponseDTO findByIdCategoria(Long id);
    List<CategoriaResponseDTO> traerTodoCategoria();
}
