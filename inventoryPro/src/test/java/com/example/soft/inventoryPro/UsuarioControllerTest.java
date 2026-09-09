package com.example.soft.inventoryPro;

import com.example.soft.inventoryPro.Usuario.dto.UsuarioRequestDTO;
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
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registrarUsuario_Valido_Retorna201() throws Exception {
        UsuarioRequestDTO dto = UsuarioRequestDTO.builder()
                .documento(12345678)
                .nombre("Carlos")
                .apellido("Gómez")
                .correo("carlos@example.com")
                .celular("3112223344")
                .direccion("Carrera 10")
                .estado("ACTIVO")
                .build();

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").exists())
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.correo").value("carlos@example.com"));
    }

    @Test
    void registrarUsuario_Invalido_Retorna400() throws Exception {
        UsuarioRequestDTO dto = UsuarioRequestDTO.builder()
                .nombre("")
                .correo("email-invalido")
                .build();

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.details.documento").exists())
                .andExpect(jsonPath("$.details.nombre").exists())
                .andExpect(jsonPath("$.details.correo").exists());
    }
}
