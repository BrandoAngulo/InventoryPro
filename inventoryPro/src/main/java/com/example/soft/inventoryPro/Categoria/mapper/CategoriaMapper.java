package com.example.soft.inventoryPro.Categoria.mapper;

import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import com.example.soft.inventoryPro.Categoria.dto.CategoriaRequestDTO;
import com.example.soft.inventoryPro.Categoria.dto.CategoriaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaRequestDTO dto) {
        if (dto == null) return null;
        return Categoria.builder()
                .nombreCategoria(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }

    public CategoriaResponseDTO toDto(Categoria entity) {
        if (entity == null) return null;
        return CategoriaResponseDTO.builder()
                .id(entity.getIdCategoria() != null ? entity.getIdCategoria().longValue() : null)
                .nombre(entity.getNombreCategoria())
                .descripcion(entity.getDescripcion())
                .build();
    }

    public void updateEntityFromDto(CategoriaRequestDTO dto, Categoria entity) {
        if (dto == null || entity == null) return;
        entity.setNombreCategoria(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
    }
}
