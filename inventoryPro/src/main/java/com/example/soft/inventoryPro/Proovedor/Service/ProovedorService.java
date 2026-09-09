package com.example.soft.inventoryPro.Proovedor.Service;

import com.example.soft.inventoryPro.Proovedor.dto.ProveedorRequestDTO;
import com.example.soft.inventoryPro.Proovedor.dto.ProveedorResponseDTO;

import java.util.List;

public interface ProovedorService {
    ProveedorResponseDTO registrarProovedor(ProveedorRequestDTO dto);
    ProveedorResponseDTO actualizarProovedor(Long id, ProveedorRequestDTO dto);
    void eliminarProovedor(Long id);
    ProveedorResponseDTO findByIdProovedor(Long id);
    List<ProveedorResponseDTO> findByAllProovedor();
    ProveedorResponseDTO cambiarEstado(Long id);
}
