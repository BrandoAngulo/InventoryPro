package com.example.soft.inventoryPro.Factura.Service;

import com.example.soft.inventoryPro.venta.dto.FacturaRequestDTO;
import com.example.soft.inventoryPro.venta.dto.FacturaResponseDTO;

import java.util.List;

public interface FacturaService {
    FacturaResponseDTO registrarFactura(FacturaRequestDTO dto);
    FacturaResponseDTO obtenerFacturaPorId(Long id);
    List<FacturaResponseDTO> obtenerFacturas();
    List<FacturaResponseDTO> listarUltimasVentas(int limite);
}
