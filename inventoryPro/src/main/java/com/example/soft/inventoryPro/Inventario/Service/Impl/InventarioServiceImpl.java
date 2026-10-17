package com.example.soft.inventoryPro.Inventario.Service.Impl;

import com.example.soft.inventoryPro.Inventario.Models.AjusteInventario;
import com.example.soft.inventoryPro.Inventario.Models.MovimientoInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoAjuste;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import com.example.soft.inventoryPro.Inventario.Repository.AjusteInventarioRepository;
import com.example.soft.inventoryPro.Inventario.Repository.MovimientoInventarioRepository;
import com.example.soft.inventoryPro.Inventario.Service.InventarioService;
import com.example.soft.inventoryPro.Inventario.dto.*;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Productos.dto.ProductoResponseDTO;
import com.example.soft.inventoryPro.common.exception.BusinessRuleException;
import com.example.soft.inventoryPro.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventarioServiceImpl implements InventarioService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final AjusteInventarioRepository ajusteInventarioRepository;

    @Override
    public List<MovimientoInventarioResponseDTO> getMovimientos(Long productoId, TipoMovimiento tipo) {
        List<MovimientoInventario> movimientos;
        if (productoId != null && tipo != null) {
            movimientos = movimientoRepository.findByProducto_IdProductosAndTipoMovimiento(productoId, tipo);
        } else if (productoId != null) {
            movimientos = movimientoRepository.findByProducto_IdProductos(productoId);
        } else if (tipo != null) {
            movimientos = movimientoRepository.findByTipoMovimiento(tipo);
        } else {
            movimientos = movimientoRepository.findAllWithProductoOrderByFechaDesc();
        }

        return movimientos.stream()
                .map(this::toMovimientoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductoResponseDTO> getProductos(Boolean bajoStock) {
        List<Productos> productos;
        if (Boolean.TRUE.equals(bajoStock)) {
            productos = productoRepository.findProductosBajoStock();
        } else {
            productos = productoRepository.findAll();
        }
        return productos.stream()
                .map(this::toProductoResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<RegistroInicialResponseDTO> registrarStockInicial(RegistroInicialRequestDTO request) {
        List<RegistroInicialResponseDTO> respuestas = new ArrayList<>();
        String observacion = (request.getObservacion() != null && !request.getObservacion().isBlank())
                ? request.getObservacion()
                : "Carga inicial de inventario";

        for (DetalleRegistroInicialRequestDTO item : request.getItems()) {
            Productos p = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + item.getProductoId()));

            int stockActual = p.getCantidad() != null ? p.getCantidad() : 0;
            Long cppActual = p.getCostoPromedio() != null ? p.getCostoPromedio() : 0L;

            int cantIngresada = item.getCantidad();
            Long costoIngreso = item.getCostoUnitarioIngreso();
            Long nuevoCPP;

            if (stockActual == 0 || cppActual == 0L) {
                nuevoCPP = costoIngreso;
            } else {
                nuevoCPP = dividirRedondeado((stockActual * cppActual) + ((long) cantIngresada * costoIngreso), stockActual + cantIngresada);
            }

            int nuevoStock = stockActual + cantIngresada;
            p.setCantidad(nuevoStock);
            p.setCostoPromedio(nuevoCPP);
            productoRepository.save(p);

            MovimientoInventario mov = MovimientoInventario.builder()
                    .producto(p)
                    .tipoMovimiento(TipoMovimiento.INVENTARIO_INICIAL)
                    .cantidad(cantIngresada)
                    .precioUnitario(costoIngreso)
                    .costoUnitario(nuevoCPP)
                    .observacion(observacion)
                    .fecha(LocalDateTime.now())
                    .build();
            movimientoRepository.save(mov);

            respuestas.add(RegistroInicialResponseDTO.builder()
                    .productoId(p.getIdProductos())
                    .productoCodigo(p.getCodigo())
                    .productoNombre(p.getNombreProducto())
                    .stockAnterior(stockActual)
                    .cantidadIngresada(cantIngresada)
                    .stockResultante(nuevoStock)
                    .costoIngreso(costoIngreso)
                    .cppAnterior(cppActual)
                    .cppResultante(nuevoCPP)
                    .fecha(mov.getFecha())
                    .build());
        }

        return respuestas;
    }

    @Override
    @Transactional
    public AjusteStockResponseDTO registrarAjusteStock(AjusteStockRequestDTO request) {
        Productos p = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + request.getProductoId()));

        int stockActual = p.getCantidad() != null ? p.getCantidad() : 0;
        Long cppActual = p.getCostoPromedio() != null ? p.getCostoPromedio() : 0L;
        int cantidadAjuste = request.getCantidad();

        Long nuevoCPP = cppActual;
        int nuevoStock;
        TipoMovimiento tipoMovimiento;

        if (request.getTipoAjuste() == TipoAjuste.POSITIVO) {
            Long costoIngreso = request.getCostoUnitarioIngreso();
            if (costoIngreso != null && costoIngreso > 0L) {
                if (stockActual == 0 || cppActual == 0L) {
                    nuevoCPP = costoIngreso;
                } else {
                    nuevoCPP = dividirRedondeado((stockActual * cppActual) + ((long) cantidadAjuste * costoIngreso), stockActual + cantidadAjuste);
                }
            }
            nuevoStock = stockActual + cantidadAjuste;
            tipoMovimiento = TipoMovimiento.AJUSTE_POSITIVO;
        } else {
            if (cantidadAjuste > stockActual) {
                throw new BusinessRuleException(
                        "Stock insuficiente para realizar el ajuste negativo. Stock actual: " + stockActual + ", cantidad requerida: " + cantidadAjuste);
            }
            nuevoStock = stockActual - cantidadAjuste;
            tipoMovimiento = TipoMovimiento.AJUSTE_NEGATIVO;
        }

        p.setCantidad(nuevoStock);
        p.setCostoPromedio(nuevoCPP);
        productoRepository.save(p);

        AjusteInventario ajuste = AjusteInventario.builder()
                .producto(p)
                .tipoAjuste(request.getTipoAjuste())
                .cantidad(cantidadAjuste)
                .costoUnitarioIngreso(request.getCostoUnitarioIngreso())
                .costoPromedioAnterior(cppActual)
                .costoPromedioResultante(nuevoCPP)
                .stockAnterior(stockActual)
                .stockResultante(nuevoStock)
                .motivo(request.getMotivo())
                .fecha(LocalDateTime.now())
                .build();
        ajuste = ajusteInventarioRepository.save(ajuste);

        Long precioMovimiento = (request.getCostoUnitarioIngreso() != null) ? request.getCostoUnitarioIngreso() : cppActual;

        MovimientoInventario mov = MovimientoInventario.builder()
                .producto(p)
                .tipoMovimiento(tipoMovimiento)
                .cantidad(cantidadAjuste)
                .precioUnitario(precioMovimiento)
                .costoUnitario(nuevoCPP)
                .referenciaId(ajuste.getId())
                .observacion(request.getMotivo())
                .fecha(LocalDateTime.now())
                .build();
        movimientoRepository.save(mov);

        return AjusteStockResponseDTO.builder()
                .id(ajuste.getId())
                .productoId(p.getIdProductos())
                .productoCodigo(p.getCodigo())
                .productoNombre(p.getNombreProducto())
                .tipoAjuste(ajuste.getTipoAjuste())
                .cantidad(ajuste.getCantidad())
                .costoUnitarioIngreso(ajuste.getCostoUnitarioIngreso())
                .costoPromedioAnterior(ajuste.getCostoPromedioAnterior())
                .costoPromedioResultante(ajuste.getCostoPromedioResultante())
                .stockAnterior(ajuste.getStockAnterior())
                .stockResultante(ajuste.getStockResultante())
                .motivo(ajuste.getMotivo())
                .fecha(ajuste.getFecha())
                .build();
    }

    @Override
    public List<AjusteStockResponseDTO> getAjustes(Long productoId, TipoAjuste tipo) {
        List<AjusteInventario> ajustes;
        if (productoId != null && tipo != null) {
            ajustes = ajusteInventarioRepository.findByProducto_IdProductosAndTipoAjuste(productoId, tipo);
        } else if (productoId != null) {
            ajustes = ajusteInventarioRepository.findByProducto_IdProductos(productoId);
        } else if (tipo != null) {
            ajustes = ajusteInventarioRepository.findByTipoAjuste(tipo);
        } else {
            ajustes = ajusteInventarioRepository.findAllByOrderByFechaDesc();
        }

        return ajustes.stream()
                .map(this::toAjusteDTO)
                .collect(Collectors.toList());
    }

    // División entera redondeada al centavo más cercano (mitad hacia arriba); valores positivos
    private Long dividirRedondeado(long dividendo, long divisor) {
        return (dividendo + divisor / 2) / divisor;
    }

    private AjusteStockResponseDTO toAjusteDTO(AjusteInventario a) {
        Productos p = a.getProducto();
        return AjusteStockResponseDTO.builder()
                .id(a.getId())
                .productoId(p != null ? p.getIdProductos() : null)
                .productoCodigo(p != null ? p.getCodigo() : null)
                .productoNombre(p != null ? p.getNombreProducto() : null)
                .tipoAjuste(a.getTipoAjuste())
                .cantidad(a.getCantidad())
                .costoUnitarioIngreso(a.getCostoUnitarioIngreso())
                .costoPromedioAnterior(a.getCostoPromedioAnterior())
                .costoPromedioResultante(a.getCostoPromedioResultante())
                .stockAnterior(a.getStockAnterior())
                .stockResultante(a.getStockResultante())
                .motivo(a.getMotivo())
                .fecha(a.getFecha())
                .build();
    }

    private MovimientoInventarioResponseDTO toMovimientoDTO(MovimientoInventario m) {
        Productos p = m.getProducto();
        return MovimientoInventarioResponseDTO.builder()
                .id(m.getId())
                .productoId(p != null ? p.getIdProductos() : null)
                .productoCodigo(p != null ? p.getCodigo() : null)
                .productoNombre(p != null ? p.getNombreProducto() : null)
                .tipoMovimiento(m.getTipoMovimiento())
                .cantidad(m.getCantidad())
                .precioUnitario(m.getPrecioUnitario())
                .costoUnitario(m.getCostoUnitario())
                .fecha(m.getFecha())
                .referenciaId(m.getReferenciaId())
                .observacion(m.getObservacion())
                .build();
    }

    private ProductoResponseDTO toProductoResponseDTO(Productos p) {
        Long id = p.getIdProductos();
        String catNombre = p.getCategorias() != null && !p.getCategorias().isEmpty() ? p.getCategorias().iterator().next().getNombre() : null;
        Long catId = p.getCategorias() != null && !p.getCategorias().isEmpty() ? p.getCategorias().iterator().next().getIdCategoria() : null;
        String provNombre = p.getProveedores() != null && !p.getProveedores().isEmpty() ? p.getProveedores().iterator().next().getNombreProovedor() : null;
        Long provId = p.getProveedores() != null && !p.getProveedores().isEmpty() ? p.getProveedores().iterator().next().getIdProovedor() : null;

        return ProductoResponseDTO.builder()
                .id(id)
                .codigo(p.getCodigo())
                .nombre(p.getNombreProducto())
                .descripcion(p.getObservacion())
                .precioVenta(p.getPrecioVenta())
                .costoPromedio(p.getCostoPromedio())
                .categoriaId(catId)
                .categoriaNombre(catNombre)
                .proveedorId(provId)
                .proveedorNombre(provNombre)
                .stockActual(p.getCantidad() != null ? p.getCantidad() : 0)
                .stockMinimo(p.getStockMinimo())
                .activo(p.getActivo())
                .build();
    }
}
