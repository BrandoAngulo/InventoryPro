package com.example.soft.inventoryPro;

import com.example.soft.inventoryPro.Inventario.Models.AjusteInventario;
import com.example.soft.inventoryPro.Inventario.Models.MovimientoInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoAjuste;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import com.example.soft.inventoryPro.Inventario.Repository.AjusteInventarioRepository;
import com.example.soft.inventoryPro.Inventario.Repository.MovimientoInventarioRepository;
import com.example.soft.inventoryPro.Inventario.dto.AjusteStockRequestDTO;
import com.example.soft.inventoryPro.Inventario.dto.DetalleRegistroInicialRequestDTO;
import com.example.soft.inventoryPro.Inventario.dto.RegistroInicialRequestDTO;
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
class InventarioModulo1IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Autowired
    private AjusteInventarioRepository ajusteInventarioRepository;

    private Productos productoZero;
    private Productos productoExistente;

    @BeforeEach
    void setUp() {
        productoZero = productoRepository.save(Productos.builder()
                .codigo("PROD-ZERO")
                .nombreProducto("Aceite Premier 1L")
                .precioCompra(0)
                .precioVenta(12000)
                .costoPromedio(BigDecimal.ZERO)
                .cantidad(0)
                .stockMinimo(5)
                .activo(true)
                .build());

        productoExistente = productoRepository.save(Productos.builder()
                .codigo("PROD-EXIST")
                .nombreProducto("Harina Haz de Oros 1Kg")
                .precioCompra(3000)
                .precioVenta(5000)
                .costoPromedio(new BigDecimal("3000.00"))
                .cantidad(10)
                .stockMinimo(5)
                .activo(true)
                .build());
    }

    @Test
    void registroInicial_ConStockCero_EstableceCPPDirecto() throws Exception {
        DetalleRegistroInicialRequestDTO detalle = DetalleRegistroInicialRequestDTO.builder()
                .productoId(productoZero.getIdProductos().longValue())
                .cantidad(20)
                .costoUnitarioIngreso(new BigDecimal("8500.00"))
                .build();

        RegistroInicialRequestDTO request = RegistroInicialRequestDTO.builder()
                .observacion("Carga inicial de aceite")
                .items(List.of(detalle))
                .build();

        mockMvc.perform(post("/inventario/registro-inicial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].stockAnterior").value(0))
                .andExpect(jsonPath("$[0].cantidadIngresada").value(20))
                .andExpect(jsonPath("$[0].stockResultante").value(20))
                .andExpect(jsonPath("$[0].cppResultante").value(8500.00));

        Productos actualizado = productoRepository.findById(productoZero.getIdProductos()).orElseThrow();
        assertEquals(20, actualizado.getCantidad());
        assertEquals(new BigDecimal("8500.00"), actualizado.getCostoPromedio());

        List<MovimientoInventario> movs = movimientoInventarioRepository.findByProducto_IdProductos(productoZero.getIdProductos());
        assertFalse(movs.isEmpty());
        assertEquals(TipoMovimiento.INVENTARIO_INICIAL, movs.get(0).getTipoMovimiento());
        assertEquals(20, movs.get(0).getCantidad());
    }

    @Test
    void registroInicial_ConStockExistente_RecalculaCPPExactamente() throws Exception {
        // Inicial: stock = 10, cppActual = $3000.00
        // Carga Inicial: 10 unidades a $5000.00
        // Fórmula CPP: ((10 * 3000) + (10 * 5000)) / (10 + 10) = 80000 / 20 = $4000.00
        DetalleRegistroInicialRequestDTO detalle = DetalleRegistroInicialRequestDTO.builder()
                .productoId(productoExistente.getIdProductos().longValue())
                .cantidad(10)
                .costoUnitarioIngreso(new BigDecimal("5000.00"))
                .build();

        RegistroInicialRequestDTO request = RegistroInicialRequestDTO.builder()
                .observacion("Adición inicial suplementaria")
                .items(List.of(detalle))
                .build();

        mockMvc.perform(post("/inventario/registro-inicial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].cppResultante").value(4000.00))
                .andExpect(jsonPath("$[0].stockResultante").value(20));

        Productos actualizado = productoRepository.findById(productoExistente.getIdProductos()).orElseThrow();
        assertEquals(20, actualizado.getCantidad());
        assertEquals(new BigDecimal("4000.00"), actualizado.getCostoPromedio());
    }

    @Test
    void ajustePositivo_ConCostoUnitario_RecalculaCPP() throws Exception {
        // Inicial: stock = 10, cpp = 3000.00
        // Ajuste +: 5 unidades a $6000.00
        // CPP: ((10 * 3000) + (5 * 6000)) / (15) = (30000 + 30000) / 15 = 60000 / 15 = $4000.00
        AjusteStockRequestDTO dto = AjusteStockRequestDTO.builder()
                .productoId(productoExistente.getIdProductos().longValue())
                .tipoAjuste(TipoAjuste.POSITIVO)
                .cantidad(5)
                .costoUnitarioIngreso(new BigDecimal("6000.00"))
                .motivo("Encontrado en inventario físico")
                .build();

        mockMvc.perform(post("/inventario/ajustes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoAjuste").value("POSITIVO"))
                .andExpect(jsonPath("$.stockAnterior").value(10))
                .andExpect(jsonPath("$.stockResultante").value(15))
                .andExpect(jsonPath("$.costoPromedioResultante").value(4000.00));

        Productos actualizado = productoRepository.findById(productoExistente.getIdProductos()).orElseThrow();
        assertEquals(15, actualizado.getCantidad());
        assertEquals(new BigDecimal("4000.00"), actualizado.getCostoPromedio());

        List<AjusteInventario> ajustes = ajusteInventarioRepository.findByProducto_IdProductos(productoExistente.getIdProductos());
        assertFalse(ajustes.isEmpty());
        assertEquals(TipoAjuste.POSITIVO, ajustes.get(0).getTipoAjuste());
    }

    @Test
    void ajustePositivo_SinCostoUnitario_MantieneCPPAnterior() throws Exception {
        AjusteStockRequestDTO dto = AjusteStockRequestDTO.builder()
                .productoId(productoExistente.getIdProductos().longValue())
                .tipoAjuste(TipoAjuste.POSITIVO)
                .cantidad(5)
                .costoUnitarioIngreso(null)
                .motivo("Ajuste positivo por reconteo")
                .build();

        mockMvc.perform(post("/inventario/ajustes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stockResultante").value(15))
                .andExpect(jsonPath("$.costoPromedioResultante").value(3000.00));

        Productos actualizado = productoRepository.findById(productoExistente.getIdProductos()).orElseThrow();
        assertEquals(15, actualizado.getCantidad());
        assertEquals(new BigDecimal("3000.00"), actualizado.getCostoPromedio());
    }

    @Test
    void ajusteNegativo_ConStockSuficiente_MantieneCPP() throws Exception {
        AjusteStockRequestDTO dto = AjusteStockRequestDTO.builder()
                .productoId(productoExistente.getIdProductos().longValue())
                .tipoAjuste(TipoAjuste.NEGATIVO)
                .cantidad(4)
                .motivo("Baja por empaque dañado")
                .build();

        mockMvc.perform(post("/inventario/ajustes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoAjuste").value("NEGATIVO"))
                .andExpect(jsonPath("$.stockAnterior").value(10))
                .andExpect(jsonPath("$.stockResultante").value(6))
                .andExpect(jsonPath("$.costoPromedioResultante").value(3000.00));

        Productos actualizado = productoRepository.findById(productoExistente.getIdProductos()).orElseThrow();
        assertEquals(6, actualizado.getCantidad());
        assertEquals(new BigDecimal("3000.00"), actualizado.getCostoPromedio());
    }

    @Test
    void ajusteNegativo_ConStockInsuficiente_RetornaError() throws Exception {
        AjusteStockRequestDTO dto = AjusteStockRequestDTO.builder()
                .productoId(productoExistente.getIdProductos().longValue())
                .tipoAjuste(TipoAjuste.NEGATIVO)
                .cantidad(50) // Mayor que stock (10)
                .motivo("Baja excesiva")
                .build();

        mockMvc.perform(post("/inventario/ajustes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void consultarAjustes_RetornaHistorial() throws Exception {
        // Registrar un ajuste primero
        AjusteStockRequestDTO dto = AjusteStockRequestDTO.builder()
                .productoId(productoExistente.getIdProductos().longValue())
                .tipoAjuste(TipoAjuste.POSITIVO)
                .cantidad(2)
                .motivo("Ingreso extraordinario")
                .build();

        mockMvc.perform(post("/inventario/ajustes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/inventario/ajustes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].motivo").value("Ingreso extraordinario"));
    }
}
