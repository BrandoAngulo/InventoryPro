package com.example.soft.inventoryPro;

import com.example.soft.inventoryPro.Caja.dto.AperturaCajaRequestDTO;
import com.example.soft.inventoryPro.Caja.dto.CierreCajaRequestDTO;
import com.example.soft.inventoryPro.Caja.dto.MovimientoCajaRequestDTO;
import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Clientes.Repository.ClienteRepository;
import com.example.soft.inventoryPro.Inventario.Models.MovimientoInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import com.example.soft.inventoryPro.Inventario.Repository.MovimientoInventarioRepository;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.venta.dto.DetalleFacturaRequestDTO;
import com.example.soft.inventoryPro.venta.dto.FacturaRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class Phase4FacturasYCajaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    private Cliente cliente;
    private Productos producto;

    @BeforeEach
    void setUp() {
        cliente = clienteRepository.save(Cliente.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .documento(12345678)
                .correo("juan.perez@example.com")
                .estado("ACTIVO")
                .build());

        producto = productoRepository.save(Productos.builder()
                .codigo("PROD-002")
                .nombreProducto("Aceite Premier 1L")
                .precioCompra(8000)
                .precioVenta(10000)
                .costoPromedio(new BigDecimal("8000.00"))
                .cantidad(10)
                .stockMinimo(2)
                .activo(true)
                .build());
    }

    @Test
    void registrarFactura_SinCajaAbierta_Retorna400() throws Exception {
        FacturaRequestDTO dto = FacturaRequestDTO.builder()
                .clienteId(cliente.getIdCliente().longValue())
                .formaPago("EFECTIVO")
                .detalles(List.of(DetalleFacturaRequestDTO.builder()
                        .productoId(producto.getIdProductos().longValue())
                        .cantidad(2)
                        .precioUnitario(new BigDecimal("10000.00"))
                        .build()))
                .build();

        mockMvc.perform(post("/facturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("sin una caja abierta")));
    }

    @Test
    void registrarFactura_StockInsuficiente_Retorna400() throws Exception {
        // 1. Abrir Caja
        AperturaCajaRequestDTO aperturaDTO = AperturaCajaRequestDTO.builder()
                .montoInicial(new BigDecimal("50000.00"))
                .observacion("Apertura de turno mañana")
                .build();

        mockMvc.perform(post("/caja/aperturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aperturaDTO)))
                .andExpect(status().isCreated());

        // 2. Intentar vender 15 unidades cuando sólo hay 10 disponibles
        FacturaRequestDTO dto = FacturaRequestDTO.builder()
                .clienteId(cliente.getIdCliente().longValue())
                .formaPago("EFECTIVO")
                .detalles(List.of(DetalleFacturaRequestDTO.builder()
                        .productoId(producto.getIdProductos().longValue())
                        .cantidad(15)
                        .precioUnitario(new BigDecimal("10000.00"))
                        .build()))
                .build();

        mockMvc.perform(post("/facturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("Stock insuficiente")))
                .andExpect(jsonPath("$.details.productoId").value(String.valueOf(producto.getIdProductos())))
                .andExpect(jsonPath("$.details.stockDisponible").value("10"))
                .andExpect(jsonPath("$.details.cantidadSolicitada").value("15"));
    }

    @Test
    void flujoCompleto_AperturaFacturaMovimientoCierreCaja() throws Exception {
        // 1. Abrir Caja con $50,000.00
        AperturaCajaRequestDTO apertura = AperturaCajaRequestDTO.builder()
                .montoInicial(new BigDecimal("50000.00"))
                .observacion("Apertura inicial")
                .build();

        mockMvc.perform(post("/caja/aperturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apertura)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.montoInicial").value(50000.00))
                .andExpect(jsonPath("$.estado").value("ABIERTA"));

        // Validar que intentar abrir una 2da caja falla con 400
        mockMvc.perform(post("/caja/aperturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apertura)))
                .andExpect(status().isBadRequest());

        // Consultar caja actual
        mockMvc.perform(get("/caja/actual"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ABIERTA"));

        // 2. Realizar una Venta por 3 unidades a $10,000 c/u = $30,000.00
        FacturaRequestDTO facturaDTO = FacturaRequestDTO.builder()
                .clienteId(cliente.getIdCliente().longValue())
                .formaPago("EFECTIVO")
                .observacion("Venta pos de prueba")
                .detalles(List.of(DetalleFacturaRequestDTO.builder()
                        .productoId(producto.getIdProductos().longValue())
                        .cantidad(3)
                        .precioUnitario(new BigDecimal("10000.00"))
                        .build()))
                .build();

        mockMvc.perform(post("/facturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facturaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total").value(30000.00))
                .andExpect(jsonPath("$.detalles", hasSize(1)));

        // Verificar que el stock disminuyó de 10 a 7
        Productos prodModificado = productoRepository.findById(producto.getIdProductos()).orElseThrow();
        assertEquals(7, prodModificado.getCantidad(), "El stock debe ser 10 - 3 = 7");

        // Verificar que se registró el Kardex VENTA
        List<MovimientoInventario> movs = movimientoInventarioRepository.findByProducto_IdProductosAndTipoMovimiento(
                producto.getIdProductos(), TipoMovimiento.VENTA);
        assertFalse(movs.isEmpty());
        assertEquals(3, movs.get(0).getCantidad());

        // 3. Registrar un Egreso Manual de $10,000.00
        MovimientoCajaRequestDTO egresoDTO = MovimientoCajaRequestDTO.builder()
                .tipo("EGRESO_MANUAL")
                .monto(new BigDecimal("10000.00"))
                .concepto("Pago insumos menores")
                .build();

        mockMvc.perform(post("/caja/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(egresoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.monto").value(10000.00));

        // 4. Cerrar la Caja declarando $70,000.00 contado
        // Esperado = 50,000 (inicial) + 30,000 (ventas) - 10,000 (egresos) = 70,000.00
        // Diferencia = 70,000 - 70,000 = 0.00
        CierreCajaRequestDTO cierreDTO = CierreCajaRequestDTO.builder()
                .montoCierre(new BigDecimal("70000.00"))
                .observacion("Cierre perfecto de turno")
                .build();

        mockMvc.perform(post("/caja/cierres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cierreDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.montoInicial").value(50000.00))
                .andExpect(jsonPath("$.montoIngresos").value(30000.00))
                .andExpect(jsonPath("$.montoEgresos").value(10000.00))
                .andExpect(jsonPath("$.montoEsperado").value(70000.00))
                .andExpect(jsonPath("$.montoCierre").value(70000.00))
                .andExpect(jsonPath("$.diferencia").value(0.00))
                .andExpect(jsonPath("$.estado").value("CERRADA"));
    }
}
