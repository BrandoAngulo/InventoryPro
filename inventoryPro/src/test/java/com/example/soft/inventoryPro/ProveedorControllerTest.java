package com.example.soft.inventoryPro;

import com.example.soft.inventoryPro.Proovedor.dto.ProveedorRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registrarProveedor_Exitoso_Retorna201() throws Exception {
        ProveedorRequestDTO dto = ProveedorRequestDTO.builder()
                .documento("900123456")
                .nombre("Distribuidora Alpha")
                .telefono("3009998877")
                .correo("contacto@alpha.com")
                .direccion("Calle 10 #20-30")
                .ciudad("Bogotá")
                .build();

        mockMvc.perform(post("/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Distribuidora Alpha"))
                .andExpect(jsonPath("$.documento").value("900123456"));
    }

    @Test
    void registrarProveedor_DocumentoDuplicado_Retorna409() throws Exception {
        ProveedorRequestDTO dto1 = ProveedorRequestDTO.builder()
                .documento("900888777")
                .nombre("Proveedor Original")
                .build();

        mockMvc.perform(post("/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        ProveedorRequestDTO dto2 = ProveedorRequestDTO.builder()
                .documento("900888777")
                .nombre("Proveedor Duplicado")
                .build();

        mockMvc.perform(post("/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isConflict());
    }

    @Test
    void actualizarProveedor_DocumentoExistenteDeOtroProveedor_Retorna409() throws Exception {
        ProveedorRequestDTO p1 = ProveedorRequestDTO.builder()
                .documento("900111222")
                .nombre("Proveedor Uno")
                .build();

        mockMvc.perform(post("/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p1)))
                .andExpect(status().isCreated());

        ProveedorRequestDTO p2 = ProveedorRequestDTO.builder()
                .documento("900333444")
                .nombre("Proveedor Dos")
                .build();

        MvcResult result2 = mockMvc.perform(post("/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p2)))
                .andExpect(status().isCreated())
                .andReturn();

        String response2 = result2.getResponse().getContentAsString();
        Long idP2 = objectMapper.readTree(response2).get("id").asLong();

        ProveedorRequestDTO updateP2 = ProveedorRequestDTO.builder()
                .documento("900111222")
                .nombre("Proveedor Dos Modificado")
                .build();

        mockMvc.perform(put("/proveedores/" + idP2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateP2)))
                .andExpect(status().isConflict());
    }
}
