package com.example.soft.inventoryPro.Compra.Service;

import com.example.soft.inventoryPro.Compra.dto.CompraRequestDTO;
import com.example.soft.inventoryPro.Compra.dto.CompraResponseDTO;

import java.util.List;

public interface CompraService {
    CompraResponseDTO registrarCompra(CompraRequestDTO dto);
    CompraResponseDTO findByIdCompra(Integer id);
    List<CompraResponseDTO> findByAllCompra();
    List<CompraResponseDTO> listarUltimasCompras(int limite);
}
