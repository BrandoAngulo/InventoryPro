package com.example.soft.inventoryPro.Dashboard;

import com.example.soft.inventoryPro.Caja.Models.Caja;
import com.example.soft.inventoryPro.Caja.Repository.CajaRepository;
import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Clientes.Repository.ClienteRepository;
import com.example.soft.inventoryPro.Compra.Repository.CompraRepository;
import com.example.soft.inventoryPro.Factura.Repository.FacturaRepository;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private CajaRepository cajaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        facturaRepository.deleteAll();
        compraRepository.deleteAll();
        cajaRepository.deleteAll();
        productoRepository.deleteAll();
        clienteRepository.deleteAll();

        Caja caja = Caja.builder()
                .montoInicial(new BigDecimal("100.00"))
                .montoIngresos(new BigDecimal("50.00"))
                .montoEgresos(BigDecimal.ZERO)
                .montoEsperado(new BigDecimal("150.00"))
                .estado("ABIERTA")
                .fechaApertura(LocalDateTime.now())
                .build();
        cajaRepository.save(caja);

        Cliente cliente = Cliente.builder()
                .nombre("Cliente Test")
                .documento(123456789)
                .estado("ACTIVO")
                .build();
        clienteRepository.save(cliente);

        Productos producto = Productos.builder()
                .codigo("PROD-LOW")
                .nombreProducto("Producto Stock Bajo")
                .precioVenta(25)
                .costoPromedio(new BigDecimal("15.00"))
                .cantidad(2)
                .stockMinimo(5)
                .activo(true)
                .build();
        productoRepository.save(producto);
    }

    @Test
    @DisplayName("GET /dashboard/resumen - Retorna KPIs correctamente con base de datos H2")
    void testObtenerResumenDashboard() throws Exception {
        mockMvc.perform(get("/dashboard/resumen")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCajaActual", is(150.00)))
                .andExpect(jsonPath("$.clientesActivosCount", is(1)))
                .andExpect(jsonPath("$.productosBajoStockCount", is(1)))
                .andExpect(jsonPath("$.productosBajoStock[0].codigo", is("PROD-LOW")))
                .andExpect(jsonPath("$.ventasDelDia", notNullValue()));
    }
}
