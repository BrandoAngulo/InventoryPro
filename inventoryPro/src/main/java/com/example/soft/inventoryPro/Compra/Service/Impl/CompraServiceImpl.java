package com.example.soft.inventoryPro.Compra.Service.Impl;

import com.example.soft.inventoryPro.Compra.Models.Compra;
import com.example.soft.inventoryPro.Compra.Models.DetalleCompra;
import com.example.soft.inventoryPro.Compra.Repository.CompraRepository;
import com.example.soft.inventoryPro.Compra.Service.CompraService;
import com.example.soft.inventoryPro.Compra.dto.CompraRequestDTO;
import com.example.soft.inventoryPro.Compra.dto.CompraResponseDTO;
import com.example.soft.inventoryPro.Compra.dto.DetalleCompraRequestDTO;
import com.example.soft.inventoryPro.Compra.mapper.CompraMapper;
import com.example.soft.inventoryPro.Inventario.Models.MovimientoInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import com.example.soft.inventoryPro.Inventario.Repository.MovimientoInventarioRepository;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import com.example.soft.inventoryPro.Proovedor.Repository.ProovedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;
    private final ProovedorRepository proveedorRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    @Override
    @Transactional
    public CompraResponseDTO registrarCompra(CompraRequestDTO dto) {
        Long provId = dto.getProveedorId();
        Proovedor proveedor = proveedorRepository.findById(provId.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con ID: " + provId));

        Compra compra = Compra.builder()
                .numeroFacturaProveedor(dto.getNumeroFacturaProveedor())
                .observacion(dto.getObservacion())
                .fechaCompra(LocalDateTime.now())
                .proovedor(proveedor)
                .detalles(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        // Save compra initial state to obtain ID for kardex reference
        compra = compraRepository.save(compra);

        for (DetalleCompraRequestDTO detalleDTO : dto.getDetalles()) {
            Long prodId = detalleDTO.getProductoId();
            Productos producto = productoRepository.findById(prodId.intValue())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + prodId));

            int stockActual = producto.getCantidad() != null ? producto.getCantidad() : 0;
            int cantComprada = detalleDTO.getCantidad();
            BigDecimal precioCompra = detalleDTO.getPrecioUnitario();
            BigDecimal cppActual = producto.getCostoPromedio() != null ? producto.getCostoPromedio() : BigDecimal.ZERO;

            // Recálculo exacto del Costo Promedio Ponderado (CPP)
            // nuevoCPP = ((stockActual * cppActual) + (cantComprada * precioCompra)) / (stockActual + cantComprada)
            BigDecimal numerador = BigDecimal.valueOf(stockActual).multiply(cppActual)
                    .add(BigDecimal.valueOf(cantComprada).multiply(precioCompra));
            int stockNuevo = stockActual + cantComprada;
            BigDecimal nuevoCPP = numerador.divide(BigDecimal.valueOf(stockNuevo), 4, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);

            producto.setCostoPromedio(nuevoCPP);
            producto.setCantidad(stockNuevo);
            producto.setPrecioCompra(precioCompra.setScale(0, RoundingMode.HALF_UP).intValue());
            productoRepository.save(producto);

            BigDecimal subtotal = precioCompra.multiply(BigDecimal.valueOf(cantComprada)).setScale(2, RoundingMode.HALF_UP);
            total = total.add(subtotal);

            DetalleCompra detalle = DetalleCompra.builder()
                    .compra(compra)
                    .producto(producto)
                    .cantidad(cantComprada)
                    .precioCompra(precioCompra)
                    .subtotal(subtotal)
                    .build();

            compra.getDetalles().add(detalle);

            // Kardex Movement
            MovimientoInventario movimiento = MovimientoInventario.builder()
                    .producto(producto)
                    .tipoMovimiento(TipoMovimiento.COMPRA)
                    .cantidad(cantComprada)
                    .precioUnitario(precioCompra)
                    .costoUnitario(nuevoCPP)
                    .fecha(LocalDateTime.now())
                    .referenciaId(compra.getId())
                    .observacion("Compra #" + compra.getId() + " a proveedor: " + proveedor.getNombreProovedor())
                    .build();

            movimientoInventarioRepository.save(movimiento);
        }

        compra.setTotal(total);
        Compra guardada = compraRepository.save(compra);
        return CompraMapper.toDTO(guardada);
    }

    @Override
    public CompraResponseDTO findByIdCompra(Integer id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada con ID: " + id));
        return CompraMapper.toDTO(compra);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraResponseDTO> findByAllCompra() {
        return compraRepository.findAll().stream()
                .map(CompraMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraResponseDTO> listarUltimasCompras(int limite) {
        return compraRepository.findAll(PageRequest.of(0, limite, Sort.by(Sort.Direction.DESC, "fechaCompra"))).getContent()
                .stream()
                .map(CompraMapper::toDTO)
                .collect(Collectors.toList());
    }
}
