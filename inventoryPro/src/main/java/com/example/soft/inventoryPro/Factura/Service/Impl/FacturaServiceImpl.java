package com.example.soft.inventoryPro.Factura.Service.Impl;

import com.example.soft.inventoryPro.Caja.Models.Caja;
import com.example.soft.inventoryPro.Caja.Models.MovimientoCaja;
import com.example.soft.inventoryPro.Caja.Repository.CajaRepository;
import com.example.soft.inventoryPro.Caja.Repository.MovimientoCajaRepository;
import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Clientes.Repository.ClienteRepository;
import com.example.soft.inventoryPro.Factura.Models.DetalleFactura;
import com.example.soft.inventoryPro.Factura.Models.Factura;
import com.example.soft.inventoryPro.Factura.Repository.FacturaRepository;
import com.example.soft.inventoryPro.Factura.Service.FacturaService;
import com.example.soft.inventoryPro.Inventario.Models.MovimientoInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import com.example.soft.inventoryPro.Inventario.Repository.MovimientoInventarioRepository;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Ultils.Errores.StockInsuficienteException;
import com.example.soft.inventoryPro.venta.dto.*;
import lombok.RequiredArgsConstructor;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final CajaRepository cajaRepository;
    private final MovimientoCajaRepository movimientoCajaRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    private static final AtomicLong sequenceCounter = new AtomicLong(System.currentTimeMillis() % 1000000);

    @Override
    @Transactional
    public FacturaResponseDTO registrarFactura(FacturaRequestDTO dto) {
        // 1. Revisa que exista una sesión de Caja ABIERTA
        Caja caja = cajaRepository.findByEstado("ABIERTA")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede emitir factura sin una caja abierta"));

        // 2. Revisa que el Cliente exista
        Long cliId = dto.getClienteId();
        Cliente cliente = clienteRepository.findById(cliId.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + cliId));

        // 3. Valida disponibilidad de stock para todos los productos solicitados
        for (DetalleFacturaRequestDTO detalleDTO : dto.getDetalles()) {
            Long prodId = detalleDTO.getProductoId();
            Productos producto = productoRepository.findById(prodId.intValue())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + prodId));

            int stockDisponible = producto.getCantidad() != null ? producto.getCantidad() : 0;
            if (detalleDTO.getCantidad() > stockDisponible) {
                throw new StockInsuficienteException(producto.getNombreProducto(), prodId, stockDisponible, detalleDTO.getCantidad());
            }
        }

        // 4. Generar número de factura único
        String numeroFactura = "FAC-" + String.format("%06d", sequenceCounter.incrementAndGet());

        Factura factura = Factura.builder()
                .numeroFactura(numeroFactura)
                .cliente(cliente)
                .formaPago(dto.getFormaPago())
                .estado("PAGADA")
                .fecha(LocalDateTime.now())
                .observacion(dto.getObservacion())
                .subtotal(BigDecimal.ZERO)
                .impuesto(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .detalles(new ArrayList<>())
                .build();

        factura = facturaRepository.save(factura);

        BigDecimal subtotalGeneral = BigDecimal.ZERO;

        for (DetalleFacturaRequestDTO detalleDTO : dto.getDetalles()) {
            Long prodId = detalleDTO.getProductoId();
            Productos producto = productoRepository.findById(prodId.intValue()).orElseThrow();

            int stockDisponible = producto.getCantidad() != null ? producto.getCantidad() : 0;
            int cantidadSolicitada = detalleDTO.getCantidad();
            int nuevoStock = stockDisponible - cantidadSolicitada;

            // Descuento de stock
            producto.setCantidad(nuevoStock);
            productoRepository.save(producto);

            BigDecimal precioVenta = detalleDTO.getPrecioUnitario();
            BigDecimal costoUnitario = producto.getCostoPromedio() != null ? producto.getCostoPromedio() : BigDecimal.ZERO;
            BigDecimal subtotalItem = precioVenta.multiply(BigDecimal.valueOf(cantidadSolicitada)).setScale(2, RoundingMode.HALF_UP);
            subtotalGeneral = subtotalGeneral.add(subtotalItem);

            DetalleFactura detalle = DetalleFactura.builder()
                    .factura(factura)
                    .producto(producto)
                    .cantidad(cantidadSolicitada)
                    .precioUnitario(precioVenta)
                    .costoUnitario(costoUnitario)
                    .subtotal(subtotalItem)
                    .build();

            factura.getDetalles().add(detalle);

            // Kardex VENTA
            MovimientoInventario mov = MovimientoInventario.builder()
                    .producto(producto)
                    .tipoMovimiento(TipoMovimiento.VENTA)
                    .cantidad(cantidadSolicitada)
                    .precioUnitario(precioVenta)
                    .costoUnitario(costoUnitario)
                    .fecha(LocalDateTime.now())
                    .referenciaId(factura.getId())
                    .observacion("Venta Factura #" + factura.getNumeroFactura())
                    .build();

            movimientoInventarioRepository.save(mov);
        }

        factura.setSubtotal(subtotalGeneral);
        factura.setTotal(subtotalGeneral);
        Factura guardada = facturaRepository.save(factura);

        // 5. Generar automáticamente movimiento de caja INGRESO_VENTA
        caja.setMontoIngresos(caja.getMontoIngresos().add(guardada.getTotal()));
        BigDecimal esperado = caja.getMontoInicial()
                .add(caja.getMontoIngresos())
                .subtract(caja.getMontoEgresos());
        caja.setMontoEsperado(esperado);
        cajaRepository.save(caja);

        MovimientoCaja movCaja = MovimientoCaja.builder()
                .caja(caja)
                .tipo("INGRESO_VENTA")
                .monto(guardada.getTotal())
                .concepto("Venta Factura #" + guardada.getNumeroFactura())
                .facturaId(guardada.getId())
                .fecha(LocalDateTime.now())
                .build();

        movimientoCajaRepository.save(movCaja);

        return toFacturaResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaResponseDTO obtenerFacturaPorId(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada con ID: " + id));
        return toFacturaResponseDTO(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> obtenerFacturas() {
        return facturaRepository.findAll().stream()
                .map(this::toFacturaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> listarUltimasVentas(int limite) {
        return facturaRepository.findAll(PageRequest.of(0, limite, Sort.by(Sort.Direction.DESC, "fecha")))
                .getContent()
                .stream()
                .map(this::toFacturaResponseDTO)
                .collect(Collectors.toList());
    }

    public FacturaResponseDTO toFacturaResponseDTO(Factura f) {
        if (f == null) return null;
        Cliente c = f.getCliente();
        String nombreCliente = c != null ? ((c.getNombre() != null ? c.getNombre() : "") + " " + (c.getApellido() != null ? c.getApellido() : "")).trim() : null;
        String docCliente = c != null && c.getDocumento() != null ? String.valueOf(c.getDocumento()) : null;
        Long clienteId = c != null && c.getIdCliente() != null ? c.getIdCliente().longValue() : null;

        List<DetalleFacturaResponseDTO> detallesDTO = f.getDetalles() != null ? f.getDetalles().stream()
                .map(d -> DetalleFacturaResponseDTO.builder()
                        .id(d.getId())
                        .productoId(d.getProducto() != null && d.getProducto().getIdProductos() != null ? d.getProducto().getIdProductos().longValue() : null)
                        .productoCodigo(d.getProducto() != null ? d.getProducto().getCodigo() : null)
                        .productoNombre(d.getProducto() != null ? d.getProducto().getNombreProducto() : null)
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .costoUnitario(d.getCostoUnitario())
                        .subtotal(d.getSubtotal())
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();

        return FacturaResponseDTO.builder()
                .id(f.getId())
                .numeroFactura(f.getNumeroFactura())
                .clienteId(clienteId)
                .clienteNombre(nombreCliente)
                .clienteDocumento(docCliente)
                .subtotal(f.getSubtotal())
                .impuesto(f.getImpuesto())
                .total(f.getTotal())
                .formaPago(f.getFormaPago())
                .estado(f.getEstado())
                .fecha(f.getFecha())
                .observacion(f.getObservacion())
                .detalles(detallesDTO)
                .build();
    }
}
