package com.example.soft.inventoryPro.Pedido.Service.Impl;

import com.example.soft.inventoryPro.Caja.Models.Caja;
import com.example.soft.inventoryPro.Caja.Models.MovimientoCaja;
import com.example.soft.inventoryPro.Caja.Repository.CajaRepository;
import com.example.soft.inventoryPro.Caja.Repository.MovimientoCajaRepository;
import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Clientes.Repository.ClienteRepository;
import com.example.soft.inventoryPro.Factura.Models.DetalleFactura;
import com.example.soft.inventoryPro.Factura.Models.Factura;
import com.example.soft.inventoryPro.Factura.Repository.FacturaRepository;
import com.example.soft.inventoryPro.Inventario.Models.MovimientoInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import com.example.soft.inventoryPro.Inventario.Repository.MovimientoInventarioRepository;
import com.example.soft.inventoryPro.Pedido.Models.DetallePedido;
import com.example.soft.inventoryPro.Pedido.Models.EstadoPedido;
import com.example.soft.inventoryPro.Pedido.Models.Pedido;
import com.example.soft.inventoryPro.Pedido.Repository.PedidoRepository;
import com.example.soft.inventoryPro.Pedido.Service.PedidoService;
import com.example.soft.inventoryPro.Pedido.dto.*;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Secuencia.Models.TipoSecuencia;
import com.example.soft.inventoryPro.Secuencia.Service.NumeracionService;
import com.example.soft.inventoryPro.Ultils.Errores.StockInsuficienteException;
import com.example.soft.inventoryPro.common.exception.BusinessRuleException;
import com.example.soft.inventoryPro.common.exception.ResourceNotFoundException;
import com.example.soft.inventoryPro.venta.dto.DetalleFacturaResponseDTO;
import com.example.soft.inventoryPro.venta.dto.FacturaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final FacturaRepository facturaRepository;
    private final CajaRepository cajaRepository;
    private final MovimientoCajaRepository movimientoCajaRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final NumeracionService numeracionService;

    @Override
    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO dto) {
        Pedido pedido = construirPedido(dto, null);
        Pedido guardado = pedidoRepository.save(pedido);
        return toPedidoResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPedidos(EstadoPedido estado, Long clienteId) {
        List<Pedido> pedidos;
        if (estado != null && clienteId != null) {
            pedidos = pedidoRepository.findByEstadoAndClienteIdWithDetails(estado, clienteId);
        } else if (estado != null) {
            pedidos = pedidoRepository.findByEstadoWithDetails(estado);
        } else if (clienteId != null) {
            pedidos = pedidoRepository.findByClienteIdWithDetails(clienteId);
        } else {
            pedidos = pedidoRepository.findAllWithDetails();
        }
        return pedidos.stream().map(this::toPedidoResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));
        return toPedidoResponseDTO(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO editarPedido(Long id, PedidoRequestDTO dto) {
        Pedido pedidoExistente = pedidoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));

        if (pedidoExistente.getEstado() != EstadoPedido.PENDIENTE) {
            throw new BusinessRuleException("Solo se pueden editar pedidos en estado PENDIENTE");
        }

        Pedido pedidoActualizado = construirPedido(dto, pedidoExistente);
        Pedido guardado = pedidoRepository.save(pedidoActualizado);
        return toPedidoResponseDTO(guardado);
    }

    @Override
    @Transactional
    public PedidoResponseDTO cambiarEstado(Long id, CambioEstadoPedidoDTO dto) {
        Pedido pedido = pedidoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));

        if (pedido.getEstado() == EstadoPedido.FACTURADO) {
            throw new BusinessRuleException("No se puede cambiar el estado de un pedido que ya está FACTURADO");
        }

        pedido.setEstado(dto.getNuevoEstado());
        Pedido guardado = pedidoRepository.save(pedido);
        return toPedidoResponseDTO(guardado);
    }

    @Override
    @Transactional
    public FacturaResponseDTO facturarPedido(Long id, FacturarPedidoRequestDTO dto) {
        Pedido pedido = pedidoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));

        if (pedido.getEstado() == EstadoPedido.FACTURADO || pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new BusinessRuleException("El pedido no se encuentra en un estado válido para facturación");
        }

        // 1. Validar Caja Abierta
        Caja caja = cajaRepository.findByEstado("ABIERTA")
                .orElseThrow(() -> new BusinessRuleException("No se puede emitir factura sin una caja abierta"));

        // 2. Validar stock para todos los ítems del pedido
        for (DetallePedido detalle : pedido.getDetalles()) {
            Productos producto = productoRepository.findById(detalle.getProducto().getIdProductos())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + detalle.getProducto().getIdProductos()));
            int stockDisponible = producto.getCantidad() != null ? producto.getCantidad() : 0;
            if (detalle.getCantidad() > stockDisponible) {
                throw new StockInsuficienteException(producto.getNombreProducto(), producto.getIdProductos(), stockDisponible, detalle.getCantidad());
            }
        }

        // 3. Crear Factura
        String numeroFactura = numeracionService.siguiente(TipoSecuencia.FACTURA);
        Factura factura = Factura.builder()
                .numeroFactura(numeroFactura)
                .cliente(pedido.getCliente())
                .subtotal(pedido.getSubtotal())
                .impuesto(pedido.getImpuesto())
                .total(pedido.getTotal())
                .formaPago(dto.getFormaPago())
                .estado("PAGADA")
                .fecha(LocalDateTime.now())
                .observacion(dto.getObservacion() != null && !dto.getObservacion().isBlank() ? dto.getObservacion() : pedido.getObservacion())
                .detalles(new ArrayList<>())
                .build();

        factura = facturaRepository.save(factura);

        // 4. Procesar detalles, descontar stock, crear kardex
        for (DetallePedido detalle : pedido.getDetalles()) {
            Productos producto = productoRepository.findById(detalle.getProducto().getIdProductos())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + detalle.getProducto().getIdProductos()));
            int stockDisponible = producto.getCantidad() != null ? producto.getCantidad() : 0;
            int nuevoStock = stockDisponible - detalle.getCantidad();

            producto.setCantidad(nuevoStock);
            productoRepository.save(producto);

            Long descUnit = detalle.getDescuentoUnitario() != null ? detalle.getDescuentoUnitario() : 0L;
            Long precioEfectivoUnitario = detalle.getPrecioAjustado() - descUnit;
            if (precioEfectivoUnitario < 0L) {
                precioEfectivoUnitario = 0L;
            }

            Long costoUnitario = producto.getCostoPromedio() != null ? producto.getCostoPromedio() : 0L;

            DetalleFactura detalleFactura = DetalleFactura.builder()
                    .factura(factura)
                    .producto(producto)
                    .cantidad(detalle.getCantidad())
                    .precioUnitario(precioEfectivoUnitario)
                    .costoUnitario(costoUnitario)
                    .subtotal(detalle.getSubtotalRenglon())
                    .build();

            factura.getDetalles().add(detalleFactura);

            // Kardex VENTA
            MovimientoInventario mov = MovimientoInventario.builder()
                    .producto(producto)
                    .tipoMovimiento(TipoMovimiento.VENTA)
                    .cantidad(detalle.getCantidad())
                    .precioUnitario(precioEfectivoUnitario)
                    .costoUnitario(costoUnitario)
                    .fecha(LocalDateTime.now())
                    .referenciaId(factura.getId())
                    .observacion("Venta Pedido #" + pedido.getNumeroPedido() + " - Factura #" + factura.getNumeroFactura())
                    .build();

            movimientoInventarioRepository.save(mov);
        }

        factura = facturaRepository.save(factura);

        // 5. Registrar movimiento de Caja solo para efectivo: tarjeta y transferencia
        //    no entran al cajón y no deben sumar al monto esperado del arqueo
        if (esPagoEnEfectivo(factura.getFormaPago())) {
            Long ingresosActual = caja.getMontoIngresos() != null ? caja.getMontoIngresos() : 0L;
            caja.setMontoIngresos(ingresosActual + factura.getTotal());
            Long inicial = caja.getMontoInicial() != null ? caja.getMontoInicial() : 0L;
            Long egresos = caja.getMontoEgresos() != null ? caja.getMontoEgresos() : 0L;
            Long esperado = inicial + caja.getMontoIngresos() - egresos;
            caja.setMontoEsperado(esperado);
            cajaRepository.save(caja);

            MovimientoCaja movCaja = MovimientoCaja.builder()
                    .caja(caja)
                    .tipo("INGRESO_VENTA")
                    .monto(factura.getTotal())
                    .concepto("Venta Factura #" + factura.getNumeroFactura() + " (Pedido #" + pedido.getNumeroPedido() + ")")
                    .facturaId(factura.getId())
                    .fecha(LocalDateTime.now())
                    .build();

            movimientoCajaRepository.save(movCaja);
        }

        // 6. Actualizar estado del pedido a FACTURADO
        pedido.setEstado(EstadoPedido.FACTURADO);
        pedidoRepository.save(pedido);

        return toFacturaResponseDTO(factura);
    }

    private boolean esPagoEnEfectivo(String formaPago) {
        return formaPago != null && "EFECTIVO".equalsIgnoreCase(formaPago.trim());
    }

    private Pedido construirPedido(PedidoRequestDTO dto, Pedido pedidoExistente) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + dto.getClienteId()));

        Long descuentoGeneral = dto.getDescuentoGeneral() != null ? dto.getDescuentoGeneral() : 0L;
        Long impuesto = dto.getImpuesto() != null ? dto.getImpuesto() : 0L;

        Pedido pedido = pedidoExistente != null ? pedidoExistente : new Pedido();
        if (pedido.getId() == null) {
            pedido.setNumeroPedido(numeracionService.siguiente(TipoSecuencia.PEDIDO));
            pedido.setEstado(EstadoPedido.PENDIENTE);
        }
        pedido.setCliente(cliente);
        pedido.setObservacion(dto.getObservacion());
        pedido.setDescuentoGeneral(descuentoGeneral);
        pedido.setImpuesto(impuesto);

        if (pedido.getDetalles() == null) {
            pedido.setDetalles(new ArrayList<>());
        } else {
            pedido.getDetalles().clear();
        }

        Long subtotalGeneral = 0L;

        for (DetallePedidoRequestDTO dDto : dto.getDetalles()) {
            Productos producto = productoRepository.findById(dDto.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + dDto.getProductoId()));

            Long precioLista = dDto.getPrecioLista() != null ? dDto.getPrecioLista()
                    : (producto.getPrecioVenta() != null ? producto.getPrecioVenta() : 0L);
            Long precioAjustado = dDto.getPrecioAjustado() != null ? dDto.getPrecioAjustado() : precioLista;
            Long descuentoUnitario = dDto.getDescuentoUnitario() != null ? dDto.getDescuentoUnitario() : 0L;

            Long precioEfectivo = precioAjustado - descuentoUnitario;
            if (precioEfectivo < 0L) {
                throw new BusinessRuleException("El descuento unitario no puede ser mayor al precio ajustado para el producto: " + producto.getNombreProducto());
            }

            Long subtotalRenglon = precioEfectivo * dDto.getCantidad();
            subtotalGeneral += subtotalRenglon;

            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(producto)
                    .cantidad(dDto.getCantidad())
                    .precioLista(precioLista)
                    .precioAjustado(precioAjustado)
                    .descuentoUnitario(descuentoUnitario)
                    .subtotalRenglon(subtotalRenglon)
                    .build();

            pedido.getDetalles().add(detalle);
        }

        if (descuentoGeneral > subtotalGeneral) {
            throw new BusinessRuleException("El descuento general no puede ser superior al subtotal del pedido");
        }

        Long total = subtotalGeneral - descuentoGeneral + impuesto;
        if (total < 0L) {
            total = 0L;
        }

        pedido.setSubtotal(subtotalGeneral);
        pedido.setTotal(total);

        return pedido;
    }

    private PedidoResponseDTO toPedidoResponseDTO(Pedido p) {
        if (p == null) return null;
        Cliente c = p.getCliente();
        String nombreCliente = c != null ? ((c.getNombre() != null ? c.getNombre() : "") + " " + (c.getApellido() != null ? c.getApellido() : "")).trim() : null;
        String docCliente = c != null && c.getDocumento() != null ? String.valueOf(c.getDocumento()) : null;
        Long clienteId = c != null ? c.getIdCliente() : null;

        List<DetallePedidoResponseDTO> detallesDTO = p.getDetalles() != null ? p.getDetalles().stream()
                .map(d -> DetallePedidoResponseDTO.builder()
                        .id(d.getId())
                        .productoId(d.getProducto() != null ? d.getProducto().getIdProductos() : null)
                        .productoCodigo(d.getProducto() != null ? d.getProducto().getCodigo() : null)
                        .productoNombre(d.getProducto() != null ? d.getProducto().getNombreProducto() : null)
                        .cantidad(d.getCantidad())
                        .precioLista(d.getPrecioLista())
                        .precioAjustado(d.getPrecioAjustado())
                        .descuentoUnitario(d.getDescuentoUnitario())
                        .subtotalRenglon(d.getSubtotalRenglon())
                        .build())
                .collect(Collectors.toList()) : new ArrayList<>();

        return PedidoResponseDTO.builder()
                .id(p.getId())
                .numeroPedido(p.getNumeroPedido())
                .clienteId(clienteId)
                .clienteNombre(nombreCliente)
                .clienteDocumento(docCliente)
                .estado(p.getEstado())
                .subtotal(p.getSubtotal())
                .descuentoGeneral(p.getDescuentoGeneral())
                .impuesto(p.getImpuesto())
                .total(p.getTotal())
                .observacion(p.getObservacion())
                .fechaCreacion(p.getFechaCreacion())
                .fechaActualizacion(p.getFechaActualizacion())
                .detalles(detallesDTO)
                .build();
    }

    private FacturaResponseDTO toFacturaResponseDTO(Factura f) {
        if (f == null) return null;
        Cliente c = f.getCliente();
        String nombreCliente = c != null ? ((c.getNombre() != null ? c.getNombre() : "") + " " + (c.getApellido() != null ? c.getApellido() : "")).trim() : null;
        String docCliente = c != null && c.getDocumento() != null ? String.valueOf(c.getDocumento()) : null;
        Long clienteId = c != null ? c.getIdCliente() : null;

        List<DetalleFacturaResponseDTO> detallesDTO = f.getDetalles() != null ? f.getDetalles().stream()
                .map(d -> DetalleFacturaResponseDTO.builder()
                        .id(d.getId())
                        .productoId(d.getProducto() != null ? d.getProducto().getIdProductos() : null)
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
