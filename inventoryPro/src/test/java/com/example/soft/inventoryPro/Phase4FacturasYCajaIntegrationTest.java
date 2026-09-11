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
                .precioCompra(8000L)
                .precioVenta(10000L)
                .costoPromedio(8000L)
                .cantidad(10)
                .stockMinimo(2)
                .activo(true)
                .build());
    }

    @Test
    void registrarFactura_SinCajaAbierta_Retorna400() throws Exception {
        FacturaRequestDTO dto = FacturaRequestDTO.builder()
                .clienteId(cliente.getIdCliente())
                .formaPago("EFECTIVO")
                .detalles(List.of(DetalleFacturaRequestDTO.builder()
                        .productoId(producto.getIdProductos())
                        .cantidad(2)
                        .precioUnitario(10000L)
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
                .montoInicial(50000L)
                .observacion("Apertura de turno mañana")
                .build();

        mockMvc.perform(post("/caja/aperturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aperturaDTO)))
                .andExpect(status().isCreated());

        // 2. Intentar vender 15 unidades cuando sólo hay 10 disponibles
        FacturaRequestDTO dto = FacturaRequestDTO.builder()
                .clienteId(cliente.getIdCliente())
                .formaPago("EFECTIVO")
                .detalles(List.of(DetalleFacturaRequestDTO.builder()
                        .productoId(producto.getIdProductos())
                        .cantidad(15)
                        .precioUnitario(10000L)
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
        // 1. Abrir Caja con $50,000
        AperturaCajaRequestDTO apertura = AperturaCajaRequestDTO.builder()
                .montoInicial(50000L)
                .observacion("Apertura inicial")
                .build();

        mockMvc.perform(post("/caja/aperturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apertura)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.montoInicial").value(50000))
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

        // 2. Realizar una Venta por 3 unidades a $10,000 c/u = $30,000
        FacturaRequestDTO facturaDTO = FacturaRequestDTO.builder()
                .clienteId(cliente.getIdCliente())
                .formaPago("EFECTIVO")
                .observacion("Venta pos de prueba")
                .detalles(List.of(DetalleFacturaRequestDTO.builder()
                        .productoId(producto.getIdProductos())
                        .cantidad(3)
                        .precioUnitario(10000L)
                        .build()))
                .build();

        mockMvc.perform(post("/facturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facturaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total").value(30000))
                .andExpect(jsonPath("$.detalles", hasSize(1)));

        // Verificar que el stock disminuyó de 10 a 7
        Productos prodModificado = productoRepository.findById(producto.getIdProductos()).orElseThrow();
        assertEquals(7, prodModificado.getCantidad(), "El stock debe ser 10 - 3 = 7");

        // Verificar que se registró el Kardex VENTA
        List<MovimientoInventario> movs = movimientoInventarioRepository.findByProducto_IdProductosAndTipoMovimiento(
                producto.getIdProductos(), TipoMovimiento.VENTA);
        assertFalse(movs.isEmpty());
        assertEquals(3, movs.get(0).getCantidad());

        // 3. Registrar un Egreso Manual de $10,000
        MovimientoCajaRequestDTO egresoDTO = MovimientoCajaRequestDTO.builder()
                .tipo("EGRESO_MANUAL")
                .monto(10000L)
                .concepto("Pago insumos menores")
                .build();

        mockMvc.perform(post("/caja/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(egresoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.monto").value(10000));

        // 4. Cerrar la Caja declarando $70,000 contado
        // Esperado = 50,000 (inicial) + 30,000 (ventas) - 10,000 (egresos) = 70,000
        // Diferencia = 70,000 - 70,000 = 0
        CierreCajaRequestDTO cierreDTO = CierreCajaRequestDTO.builder()
                .montoCierre(70000L)
                .observacion("Cierre perfecto de turno")
                .build();

        mockMvc.perform(post("/caja/cierres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cierreDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.montoInicial").value(50000))
                .andExpect(jsonPath("$.montoIngresos").value(30000))
                .andExpect(jsonPath("$.montoEgresos").value(10000))
                .andExpect(jsonPath("$.montoEsperado").value(70000))
                .andExpect(jsonPath("$.montoCierre").value(70000))
                .andExpect(jsonPath("$.diferencia").value(0))
                .andExpect(jsonPath("$.estado").value("CERRADA"));
    }
}
