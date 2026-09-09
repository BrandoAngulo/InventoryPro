package com.example.soft.inventoryPro.Caja.Service.Impl;

import com.example.soft.inventoryPro.Caja.Models.Caja;
import com.example.soft.inventoryPro.Caja.Models.MovimientoCaja;
import com.example.soft.inventoryPro.Caja.Repository.CajaRepository;
import com.example.soft.inventoryPro.Caja.Repository.MovimientoCajaRepository;
import com.example.soft.inventoryPro.Caja.Service.CajaService;
import com.example.soft.inventoryPro.Caja.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe una sesión de caja abierta");
        }

        BigDecimal inicial = dto.getMontoInicial() != null ? dto.getMontoInicial() : BigDecimal.ZERO;

        Caja caja = Caja.builder()
                .montoInicial(inicial)
                .montoIngresos(BigDecimal.ZERO)
                .montoEgresos(BigDecimal.ZERO)
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una caja abierta en este momento"));
        return toCajaResponseDTO(caja);
    }

    @Override
    @Transactional
    public MovimientoCajaResponseDTO registrarMovimiento(MovimientoCajaRequestDTO dto) {
        Caja caja = cajaRepository.findByEstado("ABIERTA")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay una caja abierta para registrar movimientos"));

        String tipoUpper = dto.getTipo() != null ? dto.getTipo().toUpperCase() : "";
        if ("INGRESO_MANUAL".equals(tipoUpper)) {
            caja.setMontoIngresos(caja.getMontoIngresos().add(dto.getMonto()));
        } else if ("EGRESO_MANUAL".equals(tipoUpper)) {
            caja.setMontoEgresos(caja.getMontoEgresos().add(dto.getMonto()));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de movimiento no válido: " + dto.getTipo());
        }

        BigDecimal esperado = caja.getMontoInicial()
                .add(caja.getMontoIngresos())
                .subtract(caja.getMontoEgresos());
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay una caja abierta para cerrar"));

        BigDecimal esperado = caja.getMontoInicial()
                .add(caja.getMontoIngresos())
                .subtract(caja.getMontoEgresos());

        BigDecimal cierre = dto.getMontoCierre() != null ? dto.getMontoCierre() : BigDecimal.ZERO;
        BigDecimal diferencia = cierre.subtract(esperado);

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
        return movimientoCajaRepository.findByCaja_Id(cajaId).stream()
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
