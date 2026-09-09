package com.example.soft.inventoryPro.Caja.Service;

import com.example.soft.inventoryPro.Caja.dto.*;
import java.util.List;

public interface CajaService {
    CajaResponseDTO abrirCaja(AperturaCajaRequestDTO dto);
    CajaResponseDTO getCajaActual();
    MovimientoCajaResponseDTO registrarMovimiento(MovimientoCajaRequestDTO dto);
    CajaResponseDTO cerrarCaja(CierreCajaRequestDTO dto);
    List<MovimientoCajaResponseDTO> getMovimientosCaja(Long cajaId);
}
