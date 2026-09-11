package com.example.soft.inventoryPro;

import com.example.soft.inventoryPro.Compra.dto.CompraRequestDTO;
import com.example.soft.inventoryPro.Compra.dto.DetalleCompraRequestDTO;
import com.example.soft.inventoryPro.Inventario.Models.MovimientoInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import com.example.soft.inventoryPro.Inventario.Repository.MovimientoInventarioRepository;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import com.example.soft.inventoryPro.Proovedor.Repository.ProovedorRepository;
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
class Phase3InventarioYComprasIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProovedorRepository proveedorRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    private Proovedor proveedor;
    private Productos producto;

    @BeforeEach
    void setUp() {
        proveedor = proveedorRepository.save(Proovedor.builder()
                .nombreProovedor("Distribuidora Santander")
                .documento("900123456")
                .estado(true)
                .build());

        producto = productoRepository.save(Productos.builder()
                .codigo("PROD-001")
                .nombreProducto("Arroz Roa 1Kg")
                .precioCompra(100L)
                .precioVenta(250L)
                .costoPromedio(100L)
                .cantidad(10)
                .stockMinimo(5)
                .activo(true)
                .build());
    }

    @Test
    void registrarCompra_RecalculaCPPYActualizaStockYKardex() throws Exception {
        // Inicial: stock = 10, cppActual = $100
        // Compra: 10 unidades a $200
        // Fórmula CPP: ((10 * 100) + (10 * 200)) / (10 + 10) = 3000 / 20 = $150
        DetalleCompraRequestDTO detalleDTO = DetalleCompraRequestDTO.builder()
                .productoId(producto.getIdProductos())
                .cantidad(10)
                .precioUnitario(200L)
                .build();

        CompraRequestDTO dto = CompraRequestDTO.builder()
                .proveedorId(proveedor.getIdProovedor())
                .numeroFacturaProveedor("FAC-PROV-1001")
                .observacion("Compra inicial de stock")
                .detalles(List.of(detalleDTO))
                .build();

        mockMvc.perform(post("/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.total").value(2000))
                .andExpect(jsonPath("$.proveedorNombre").value("Distribuidora Santander"))
                .andExpect(jsonPath("$.detalles", hasSize(1)));

        // Verificación en BD del producto actualizado
        Productos productoActualizado = productoRepository.findById(producto.getIdProductos()).orElseThrow();
        assertEquals(20, productoActualizado.getCantidad(), "El stock actual debe ser 10 + 10 = 20");
        assertEquals(150L, productoActualizado.getCostoPromedio(), "El nuevo CPP debe ser 150");

        // Verificación de Kardex (MovimientoInventario)
        List<MovimientoInventario> movimientos = movimientoInventarioRepository.findByProducto_IdProductos(producto.getIdProductos());
        assertFalse(movimientos.isEmpty(), "Debe existir al menos un movimiento de kardex registrado");
        MovimientoInventario mov = movimientos.get(0);
        assertEquals(TipoMovimiento.COMPRA, mov.getTipoMovimiento());
        assertEquals(10, mov.getCantidad());
        assertEquals(200L, mov.getPrecioUnitario());
        assertEquals(150L, mov.getCostoUnitario());
    }

    @Test
    void consultarMovimientosYProductos() throws Exception {
        mockMvc.perform(get("/inventario/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(get("/inventario/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }
}
