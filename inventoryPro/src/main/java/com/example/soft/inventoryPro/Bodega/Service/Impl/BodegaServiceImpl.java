package com.example.soft.inventoryPro.Bodega.Service.Impl;

import com.example.soft.inventoryPro.Bodega.Models.Bodega;
import com.example.soft.inventoryPro.Bodega.Repository.BodegaRepository;
import com.example.soft.inventoryPro.Bodega.Service.BodegaService;
import com.example.soft.inventoryPro.Bodega.dto.BodegaRequestDTO;
import com.example.soft.inventoryPro.Bodega.dto.BodegaResponseDTO;
import com.example.soft.inventoryPro.Bodega.mapper.BodegaMapper;
import com.example.soft.inventoryPro.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;

    @Override
    @Transactional
    public BodegaResponseDTO registrarBodega(BodegaRequestDTO dto) {
        Bodega bodega = BodegaMapper.toEntity(dto);
        Bodega guardada = bodegaRepository.save(bodega);
        return BodegaMapper.toDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BodegaResponseDTO> traerTodasBoderga() {
        return bodegaRepository.findAll().stream()
                .map(BodegaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BodegaResponseDTO actualizarBodega(Long id, BodegaRequestDTO dto) {
        Bodega bodegaExistente = bodegaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + id));

        bodegaExistente.setNombre(dto.getNombre());
        if (dto.getFechaCreacionBodega() != null) {
            bodegaExistente.setFechaCreacionBodega(dto.getFechaCreacionBodega());
        }

        Bodega actualizada = bodegaRepository.save(bodegaExistente);
        return BodegaMapper.toDTO(actualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public BodegaResponseDTO traerIdBoderga(Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + id));
        return BodegaMapper.toDTO(bodega);
    }
}
