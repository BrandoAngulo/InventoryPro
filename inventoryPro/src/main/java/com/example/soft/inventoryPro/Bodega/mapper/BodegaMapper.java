package com.example.soft.inventoryPro.Bodega.mapper;

import com.example.soft.inventoryPro.Bodega.Models.Bodega;
import com.example.soft.inventoryPro.Bodega.dto.BodegaRequestDTO;
import com.example.soft.inventoryPro.Bodega.dto.BodegaResponseDTO;

import java.time.LocalDate;

public class BodegaMapper {

    public static Bodega toEntity(BodegaRequestDTO dto) {
        if (dto == null) return null;
        return Bodega.builder()
                .nombre(dto.getNombre())
                .fechaCreacionBodega(dto.getFechaCreacionBodega() != null ? dto.getFechaCreacionBodega() : LocalDate.now().toString())
                .build();
    }

    public static BodegaResponseDTO toDTO(Bodega entity) {
        if (entity == null) return null;
        return BodegaResponseDTO.builder()
                .idBodega(entity.getIdBodega())
                .nombre(entity.getNombre())
                .fechaCreacionBodega(entity.getFechaCreacionBodega())
                .build();
    }
}
