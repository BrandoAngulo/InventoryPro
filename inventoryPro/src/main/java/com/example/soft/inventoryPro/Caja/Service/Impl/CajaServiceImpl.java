package com.example.soft.inventoryPro.Caja.Service.Impl;

import com.example.soft.inventoryPro.Caja.Models.Caja;
import com.example.soft.inventoryPro.Caja.Models.MovimientoCaja;
import com.example.soft.inventoryPro.Caja.Repository.CajaRepository;
import com.example.soft.inventoryPro.Caja.Repository.MovimientoCajaRepository;
import com.example.soft.inventoryPro.Caja.Service.CajaService;
import com.example.soft.inventoryPro.Caja.dto.*;
import com.example.soft.inventoryPro.common.exception.BusinessRuleException;
import com.example.soft.inventoryPro.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CajaServiceImpl implements CajaService {

    private final CajaRepository cajaRepository;
    private final MovimientoCajaRepository movimientoCajaRepository;

    @Override
    @Transactional
    public CajaResponseDTO abrirCaja(AperturaCajaRequestDTO dto) {
        if (cajaRepository.findByEstado("ABIERTA").isPresent()) {
            throw new BusinessRuleException("Ya existe una sesión de caja abierta");
        }

        Long inicial = dto.getMontoInicial() != null ? dto.getMontoInicial() : 0L;

        Caja caja = Caja.builder()
                .montoInicial(inicial)
                .montoIngresos(0L)
                .montoEgresos(0L)
                .montoEsperado(inicial)
                .estado("ABIERTA")
                .fechaApertura(LocalDateTime.now())
                .observacionApertura(dto.getObservacion())
                .build();

        Caja guardada = cajaRepository.save(caja);
        return toCajaResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public CajaResponseDTO getCajaActual() {
        Caja caja = cajaRepository.findByEstado("ABIERTA")
                .orElseThrow(() -> new ResourceNotFoundException("No existe una caja abierta en este momento"));
        return toCajaResponseDTO(caja);
    }

    @Override
    @Transactional
    public MovimientoCajaResponseDTO registrarMovimiento(MovimientoCajaRequestDTO dto) {
        Caja caja = cajaRepository.findByEstado("ABIERTA")
                .orElseThrow(() -> new BusinessRuleException("No hay una caja abierta para registrar movimientos"));

        String tipoUpper = dto.getTipo() != null ? dto.getTipo().toUpperCase() : "";
        Long montoIngresos = caja.getMontoIngresos() != null ? caja.getMontoIngresos() : 0L;
        Long montoEgresos = caja.getMontoEgresos() != null ? caja.getMontoEgresos() : 0L;

        if ("INGRESO_MANUAL".equals(tipoUpper)) {
            caja.setMontoIngresos(montoIngresos + dto.getMonto());
        } else if ("EGRESO_MANUAL".equals(tipoUpper)) {
            caja.setMontoEgresos(montoEgresos + dto.getMonto());
        } else {
            throw new BusinessRuleException("Tipo de movimiento no válido: " + dto.getTipo());
        }

        Long inicial = caja.getMontoInicial() != null ? caja.getMontoInicial() : 0L;
        Long esperado = inicial + caja.getMontoIngresos() - caja.getMontoEgresos();
        caja.setMontoEsperado(esperado);
        cajaRepository.save(caja);

        MovimientoCaja mov = MovimientoCaja.builder()
                .caja(caja)
                .tipo(tipoUpper)
                .monto(dto.getMonto())
                .concepto(dto.getConcepto())
                .fecha(LocalDateTime.now())
                .build();

        MovimientoCaja guardado = movimientoCajaRepository.save(mov);
        return toMovimientoResponseDTO(guardado);
    }

    @Override
    @Transactional
    public CajaResponseDTO cerrarCaja(CierreCajaRequestDTO dto) {
        Caja caja = cajaRepository.findByEstado("ABIERTA")
                .orElseThrow(() -> new BusinessRuleException("No hay una caja abierta para cerrar"));

        Long inicial = caja.getMontoInicial() != null ? caja.getMontoInicial() : 0L;
        Long ingresos = caja.getMontoIngresos() != null ? caja.getMontoIngresos() : 0L;
        Long egresos = caja.getMontoEgresos() != null ? caja.getMontoEgresos() : 0L;
        Long esperado = inicial + ingresos - egresos;

        Long cierre = dto.getMontoCierre() != null ? dto.getMontoCierre() : 0L;
        Long diferencia = cierre - esperado;

        caja.setMontoEsperado(esperado);
        caja.setMontoCierre(cierre);
        caja.setDiferencia(diferencia);
        caja.setEstado("CERRADA");
        caja.setFechaCierre(LocalDateTime.now());
        caja.setObservacionCierre(dto.getObservacion());

        Caja guardada = cajaRepository.save(caja);
        return toCajaResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoCajaResponseDTO> getMovimientosCaja(Long cajaId) {
        return movimientoCajaRepository.findByCajaIdWithCaja(cajaId).stream()
                .map(this::toMovimientoResponseDTO)
                .collect(Collectors.toList());
    }

    public CajaResponseDTO toCajaResponseDTO(Caja caja) {
        if (caja == null) return null;
        return CajaResponseDTO.builder()
                .id(caja.getId())
                .montoInicial(caja.getMontoInicial())
                .montoIngresos(caja.getMontoIngresos())
                .montoEgresos(caja.getMontoEgresos())
                .montoEsperado(caja.getMontoEsperado())
                .montoCierre(caja.getMontoCierre())
                .diferencia(caja.getDiferencia())
                .estado(caja.getEstado())
                .fechaApertura(caja.getFechaApertura())
                .fechaCierre(caja.getFechaCierre())
                .observacionApertura(caja.getObservacionApertura())
                .observacionCierre(caja.getObservacionCierre())
                .build();
    }

    public MovimientoCajaResponseDTO toMovimientoResponseDTO(MovimientoCaja m) {
        if (m == null) return null;
        return MovimientoCajaResponseDTO.builder()
                .id(m.getId())
                .cajaId(m.getCaja() != null ? m.getCaja().getId() : null)
                .tipo(m.getTipo())
                .monto(m.getMonto())
                .concepto(m.getConcepto())
                .facturaId(m.getFacturaId())
                .fecha(m.getFecha())
                .build();
    }
}
