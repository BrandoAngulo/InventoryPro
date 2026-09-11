package com.example.soft.inventoryPro.Dashboard.Service;

import com.example.soft.inventoryPro.Caja.Models.Caja;
import com.example.soft.inventoryPro.Caja.Repository.CajaRepository;
import com.example.soft.inventoryPro.Clientes.Repository.ClienteRepository;
import com.example.soft.inventoryPro.Compra.Repository.CompraRepository;
import com.example.soft.inventoryPro.Compra.Service.CompraService;
import com.example.soft.inventoryPro.Compra.dto.CompraResponseDTO;
import com.example.soft.inventoryPro.Dashboard.dto.DashboardResumenDTO;
import com.example.soft.inventoryPro.Factura.Repository.FacturaRepository;
import com.example.soft.inventoryPro.Factura.Service.FacturaService;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Productos.Service.ProductoService;
import com.example.soft.inventoryPro.Productos.dto.ProductoResponseDTO;
import com.example.soft.inventoryPro.venta.dto.FacturaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final FacturaRepository facturaRepository;
    private final FacturaService facturaService;
    private final CompraRepository compraRepository;
    private final CompraService compraService;
    private final CajaRepository cajaRepository;
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResumenDTO obtenerResumen() {
        // 1. Rango de fechas para ventas del día actual
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDia = LocalDate.now().atTime(LocalTime.MAX);

        // 2. Cálculo del monto total de ventas del día (Facturas en estado PAGADA)
        Long ventasDelDia = facturaRepository.sumTotalByFechaBetweenAndEstado(inicioDia, finDia, "PAGADA");
        if (ventasDelDia == null) {
            ventasDelDia = 0L;
        }

        // 3. Saldo esperado en la caja abierta actual (o 0 si está cerrada)
        Optional<Caja> cajaAbiertaOpt = cajaRepository.findByEstado("ABIERTA");
        Long totalCajaActual = cajaAbiertaOpt
                .map(Caja::getMontoEsperado)
                .orElse(0L);

        // 4. Conteo y lista de productos con stock bajo (stockActual <= stockMinimo)
        List<ProductoResponseDTO> productosBajoStock = productoService.listarProductosBajoStock();
        Long productosBajoStockCount = (long) productosBajoStock.size();

        // 5. Conteo de clientes activos
        Long clientesActivosCount = clienteRepository.countByEstado("ACTIVO");

        // 6. Últimas 5 facturas registradas
        List<FacturaResponseDTO> ultimasVentas = facturaService.listarUltimasVentas(5);

        // 7. Últimas 5 compras registradas
        List<CompraResponseDTO> ultimasCompras = compraService.listarUltimasCompras(5);

        return DashboardResumenDTO.builder()
                .ventasDelDia(ventasDelDia)
                .totalCajaActual(totalCajaActual)
                .productosBajoStockCount(productosBajoStockCount)
                .clientesActivosCount(clientesActivosCount)
                .ultimasVentas(ultimasVentas)
                .ultimasCompras(ultimasCompras)
                .productosBajoStock(productosBajoStock)
                .build();
    }
}
