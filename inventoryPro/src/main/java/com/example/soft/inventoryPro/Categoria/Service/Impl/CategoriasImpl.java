package com.example.soft.inventoryPro.Categoria.Service.Impl;

import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import com.example.soft.inventoryPro.Categoria.Repository.CategoriaRepository;
import com.example.soft.inventoryPro.Categoria.Service.CategoriaService;
import com.example.soft.inventoryPro.Categoria.dto.CategoriaRequestDTO;
import com.example.soft.inventoryPro.Categoria.dto.CategoriaResponseDTO;
import com.example.soft.inventoryPro.Categoria.mapper.CategoriaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriasImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    @Transactional
    public CategoriaResponseDTO registrarCategoria(CategoriaRequestDTO dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);
        Categoria guardada = categoriaRepository.save(categoria);
        return categoriaMapper.toDto(guardada);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada con ID: " + id));

        categoriaMapper.updateEntityFromDto(dto, categoria);
        Categoria actualizada = categoriaRepository.save(categoria);
        return categoriaMapper.toDto(actualizada);
    }

    @Override
    @Transactional
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id.intValue())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id.intValue());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO findByIdCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada con ID: " + id));
        return categoriaMapper.toDto(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> traerTodoCategoria() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toDto)
                .toList();
    }
}
