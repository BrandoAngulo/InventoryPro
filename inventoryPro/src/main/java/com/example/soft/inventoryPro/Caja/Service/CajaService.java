package com.example.soft.inventoryPro.Caja.Service;

import com.example.soft.inventoryPro.Caja.Models.Caja;
import com.example.soft.inventoryPro.Caja.dto.*;
import java.util.List;

public interface CajaService {
    CajaResponseDTO abrirCaja(AperturaCajaRequestDTO dto);
    CajaResponseDTO getCajaActual();
    MovimientoCajaResponseDTO registrarMovimiento(MovimientoCajaRequestDTO dto);
    CajaResponseDTO cerrarCaja(CierreCajaRequestDTO dto);
    List<MovimientoCajaResponseDTO> getMovimientosCaja(Long cajaId);

    /**
     * Registra en la caja abierta el ingreso de una venta facturada. Solo el efectivo suma al
     * monto esperado del arqueo; tarjeta y transferencia quedan como movimiento informativo
     * para cuadrar vouchers y comprobantes. Debe invocarse dentro de la transacción de la venta.
     */
    void registrarIngresoPorVenta(Caja caja, String formaPago, Long monto, String concepto, Long facturaId);
}
