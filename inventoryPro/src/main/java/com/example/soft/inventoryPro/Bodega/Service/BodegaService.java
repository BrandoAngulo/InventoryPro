package com.example.soft.inventoryPro.Bodega.Service;

import com.example.soft.inventoryPro.Bodega.dto.BodegaRequestDTO;
import com.example.soft.inventoryPro.Bodega.dto.BodegaResponseDTO;

import java.util.List;

public interface BodegaService {
    BodegaResponseDTO registrarBodega(BodegaRequestDTO dto);
    List<BodegaResponseDTO> traerTodasBoderga();
    BodegaResponseDTO actualizarBodega(Integer id, BodegaRequestDTO dto);
    BodegaResponseDTO traerIdBoderga(Integer id);
}
