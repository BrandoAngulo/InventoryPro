package com.example.soft.inventoryPro.venta.Service;

import com.example.soft.inventoryPro.venta.dto.VentaRequestDTO;
import com.example.soft.inventoryPro.venta.dto.VentaResponseDTO;

import java.util.List;

public interface VentaService {
    VentaResponseDTO registrarVentas(VentaRequestDTO dto);
    VentaResponseDTO listarVentas(Long idVentas);
    List<VentaResponseDTO> listarTodasVentas();
}
