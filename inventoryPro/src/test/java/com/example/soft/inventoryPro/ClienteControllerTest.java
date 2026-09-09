package com.example.soft.inventoryPro;

import com.example.soft.inventoryPro.Clientes.dto.ClienteRequestDTO;
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
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registrarCliente_Valido_Retorna201() throws Exception {
        ClienteRequestDTO dto = ClienteRequestDTO.builder()
                .documento("12345678")
                .nombre("Juan")
                .apellido("Pérez")
                .correo("juan@example.com")
                .celular("3001234567")
                .direccion("Calle 123")
                .build();

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.documento").value("12345678"))
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    void registrarCliente_Invalido_Retorna400() throws Exception {
        ClienteRequestDTO dto = ClienteRequestDTO.builder()
                .documento("")
                .nombre("J")
                .correo("correo-invalido")
                .build();

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.details.documento").exists())
                .andExpect(jsonPath("$.details.nombre").exists())
                .andExpect(jsonPath("$.details.correo").exists());
    }
}
