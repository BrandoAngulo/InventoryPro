package com.example.soft.inventoryPro.Bodega.Service.Impl;

import com.example.soft.inventoryPro.Bodega.Models.Bodega;
import com.example.soft.inventoryPro.Bodega.Repository.BodegaRepository;
import com.example.soft.inventoryPro.Bodega.Service.BodegaService;
import com.example.soft.inventoryPro.Bodega.dto.BodegaRequestDTO;
import com.example.soft.inventoryPro.Bodega.dto.BodegaResponseDTO;
import com.example.soft.inventoryPro.Bodega.mapper.BodegaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;

    @Override
    public BodegaResponseDTO registrarBodega(BodegaRequestDTO dto) {
        Bodega bodega = BodegaMapper.toEntity(dto);
        Bodega guardada = bodegaRepository.save(bodega);
        return BodegaMapper.toDTO(guardada);
    }

    @Override
    public List<BodegaResponseDTO> traerTodasBoderga() {
        return bodegaRepository.findAll().stream()
                .map(BodegaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BodegaResponseDTO actualizarBodega(Integer id, BodegaRequestDTO dto) {
        Bodega bodegaExistente = bodegaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bodega no encontrada con ID: " + id));

        bodegaExistente.setNombre(dto.getNombre());
        if (dto.getFechaCreacionBodega() != null) {
            bodegaExistente.setFechaCreacionBodega(dto.getFechaCreacionBodega());
        }

        Bodega actualizada = bodegaRepository.save(bodegaExistente);
        return BodegaMapper.toDTO(actualizada);
    }

    @Override
    public BodegaResponseDTO traerIdBoderga(Integer id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bodega no encontrada con ID: " + id));
        return BodegaMapper.toDTO(bodega);
    }
}
