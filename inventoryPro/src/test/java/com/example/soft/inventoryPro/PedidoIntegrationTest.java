package com.example.soft.inventoryPro;

import com.example.soft.inventoryPro.Caja.dto.AperturaCajaRequestDTO;
import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Clientes.Repository.ClienteRepository;
import com.example.soft.inventoryPro.Inventario.Models.MovimientoInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import com.example.soft.inventoryPro.Inventario.Repository.MovimientoInventarioRepository;
import com.example.soft.inventoryPro.Pedido.Models.EstadoPedido;
import com.example.soft.inventoryPro.Pedido.Models.Pedido;
import com.example.soft.inventoryPro.Pedido.Repository.PedidoRepository;
import com.example.soft.inventoryPro.Pedido.dto.*;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
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
class PedidoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    private Cliente cliente;
    private Productos producto;

    @BeforeEach
    void setUp() {
        cliente = clienteRepository.save(Cliente.builder()
                .nombre("Carlos")
                .apellido("Gómez")
                .documento(87654321)
                .correo("carlos.gomez@example.com")
                .estado("ACTIVO")
                .build());

        producto = productoRepository.save(Productos.builder()
                .codigo("PROD-PED-01")
                .nombreProducto("Arroz Roa 1Kg")
                .precioCompra(3000)
                .precioVenta(5000)
                .costoPromedio(new BigDecimal("3000.00"))
                .cantidad(10)
                .stockMinimo(2)
                .activo(true)
                .build());
    }

    @Test
    void crearPedido_Exito() throws Exception {
        PedidoRequestDTO requestDTO = PedidoRequestDTO.builder()
                .clienteId(cliente.getIdCliente().longValue())
                .descuentoGeneral(new BigDecimal("500.00"))
                .impuesto(new BigDecimal("0.00"))
                .observacion("Pedido urgente de prueba")
                .detalles(List.of(
                        DetallePedidoRequestDTO.builder()
                                .productoId(producto.getIdProductos().longValue())
                                .cantidad(2)
                                .precioLista(new BigDecimal("5000.00"))
                                .precioAjustado(new BigDecimal("4800.00"))
                                .descuentoUnitario(new BigDecimal("300.00"))
                                .build()
                ))
                .build();

        // subtotal = 2 * (4800 - 300) = 9000.00
        // total = 9000 - 500 = 8500.00
        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.numeroPedido", startsWith("PED-")))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.subtotal").value(9000.00))
                .andExpect(jsonPath("$.descuentoGeneral").value(500.00))
                .andExpect(jsonPath("$.total").value(8500.00))
                .andExpect(jsonPath("$.detalles", hasSize(1)))
                .andExpect(jsonPath("$.detalles[0].precioAjustado").value(4800.00))
                .andExpect(jsonPath("$.detalles[0].descuentoUnitario").value(300.00))
                .andExpect(jsonPath("$.detalles[0].subtotalRenglon").value(9000.00));
    }

    @Test
    void listarYObtenerPedido_Exito() throws Exception {
        PedidoRequestDTO requestDTO = PedidoRequestDTO.builder()
                .clienteId(cliente.getIdCliente().longValue())
                .detalles(List.of(
                        DetallePedidoRequestDTO.builder()
                                .productoId(producto.getIdProductos().longValue())
                                .cantidad(1)
                                .precioLista(new BigDecimal("5000.00"))
                                .precioAjustado(new BigDecimal("5000.00"))
                                .descuentoUnitario(BigDecimal.ZERO)
                                .build()
                ))
                .build();

        String content = mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PedidoResponseDTO responseDTO = objectMapper.readValue(content, PedidoResponseDTO.class);

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

        mockMvc.perform(get("/pedidos?estado=PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));

        mockMvc.perform(get("/pedidos/" + responseDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()));
    }

    @Test
    void cambiarEstadoPedido_Exito() throws Exception {
        PedidoRequestDTO requestDTO = PedidoRequestDTO.builder()
                .clienteId(cliente.getIdCliente().longValue())
                .detalles(List.of(
                        DetallePedidoRequestDTO.builder()
                                .productoId(producto.getIdProductos().longValue())
                                .cantidad(1)
                                .precioLista(new BigDecimal("5000.00"))
                                .precioAjustado(new BigDecimal("5000.00"))
                                .build()
                ))
                .build();

        String content = mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PedidoResponseDTO created = objectMapper.readValue(content, PedidoResponseDTO.class);

        CambioEstadoPedidoDTO cambioEstadoDTO = CambioEstadoPedidoDTO.builder()
                .nuevoEstado(EstadoPedido.CONFIRMADO)
                .build();

        mockMvc.perform(patch("/pedidos/" + created.getId() + "/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cambioEstadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CONFIRMADO"));
    }

    @Test
    void facturarPedido_SinCajaAbierta_Retorna400() throws Exception {
        PedidoRequestDTO requestDTO = PedidoRequestDTO.builder()
                .clienteId(cliente.getIdCliente().longValue())
                .detalles(List.of(
                        DetallePedidoRequestDTO.builder()
                                .productoId(producto.getIdProductos().longValue())
                                .cantidad(1)
                                .precioLista(new BigDecimal("5000.00"))
                                .precioAjustado(new BigDecimal("5000.00"))
                                .build()
                ))
                .build();

        String content = mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PedidoResponseDTO created = objectMapper.readValue(content, PedidoResponseDTO.class);

        FacturarPedidoRequestDTO facturarDTO = FacturarPedidoRequestDTO.builder()
                .formaPago("EFECTIVO")
                .observacion("Facturación de prueba")
                .build();

        mockMvc.perform(post("/pedidos/" + created.getId() + "/facturar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facturarDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("sin una caja abierta")));
    }

    @Test
    void facturarPedido_FlujoCompletoExito() throws Exception {
        // 1. Abrir caja
        AperturaCajaRequestDTO apertura = AperturaCajaRequestDTO.builder()
                .montoInicial(new BigDecimal("100000.00"))
                .observacion("Apertura para facturar pedido")
                .build();

        mockMvc.perform(post("/caja/aperturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apertura)))
                .andExpect(status().isCreated());

        // 2. Crear pedido de 3 unidades
        PedidoRequestDTO requestDTO = PedidoRequestDTO.builder()
                .clienteId(cliente.getIdCliente().longValue())
                .descuentoGeneral(new BigDecimal("1000.00"))
                .detalles(List.of(
                        DetallePedidoRequestDTO.builder()
                                .productoId(producto.getIdProductos().longValue())
                                .cantidad(3)
                                .precioLista(new BigDecimal("5000.00"))
                                .precioAjustado(new BigDecimal("4500.00"))
                                .descuentoUnitario(new BigDecimal("500.00"))
                                .build()
                ))
                .build();

        // Subtotal = 3 * (4500 - 500) = 12000.00
        // Total = 12000 - 1000 = 11000.00
        String content = mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PedidoResponseDTO created = objectMapper.readValue(content, PedidoResponseDTO.class);

        // 3. Facturar pedido
        FacturarPedidoRequestDTO facturarDTO = FacturarPedidoRequestDTO.builder()
                .formaPago("EFECTIVO")
                .observacion("Facturando orden #" + created.getNumeroPedido())
                .build();

        mockMvc.perform(post("/pedidos/" + created.getId() + "/facturar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facturarDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.numeroFactura", startsWith("FAC-")))
                .andExpect(jsonPath("$.total").value(11000.00))
                .andExpect(jsonPath("$.estado").value("PAGADA"));

        // 4. Verificar que el estado del pedido cambió a FACTURADO
        Pedido pedidoActualizado = pedidoRepository.findById(created.getId()).orElseThrow();
        assertEquals(EstadoPedido.FACTURADO, pedidoActualizado.getEstado());

        // 5. Verificar descuenta de stock de 10 a 7
        Productos prodActualizado = productoRepository.findById(producto.getIdProductos()).orElseThrow();
        assertEquals(7, prodActualizado.getCantidad());

        // 6. Verificar Kardex VENTA
        List<MovimientoInventario> movs = movimientoInventarioRepository.findByProducto_IdProductosAndTipoMovimiento(
                producto.getIdProductos(), TipoMovimiento.VENTA);
        assertFalse(movs.isEmpty());
        assertEquals(3, movs.get(0).getCantidad());
    }
}
