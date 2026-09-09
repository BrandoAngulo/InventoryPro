package com.example.soft.inventoryPro;

import com.example.soft.inventoryPro.venta.dto.VentaRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registrarVenta_Invalido_Retorna400() throws Exception {
        VentaRequestDTO dto = VentaRequestDTO.builder()
                .idCliente(null)
                .idUsuario(null)
                .detalles(null)
                .build();

        mockMvc.perform(post("/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.details.idCliente").exists())
                .andExpect(jsonPath("$.details.idUsuario").exists())
                .andExpect(jsonPath("$.details.detalles").exists());
    }
}
