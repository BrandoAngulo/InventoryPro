package com.example.soft.inventoryPro;

import com.example.soft.inventoryPro.Categoria.dto.CategoriaRequestDTO;
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
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registrarCategoria_Valido_Retorna201() throws Exception {
        CategoriaRequestDTO dto = CategoriaRequestDTO.builder()
                .nombre("Electrónicos")
                .descripcion("Dispositivos electrónicos")
                .build();

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Electrónicos"))
                .andExpect(jsonPath("$.descripcion").value("Dispositivos electrónicos"));
    }

    @Test
    void registrarCategoria_Invalido_Retorna400() throws Exception {
        CategoriaRequestDTO dto = CategoriaRequestDTO.builder()
                .nombre("")
                .build();

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.details.nombre").exists());
    }
}
