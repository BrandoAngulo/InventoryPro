package com.example.soft.inventoryPro.Clientes.mapper;

import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Clientes.dto.ClienteRequestDTO;
import com.example.soft.inventoryPro.Clientes.dto.ClienteResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        if (dto == null) return null;
        Integer docInt = null;
        if (dto.getDocumento() != null && !dto.getDocumento().isBlank()) {
            try {
                docInt = Integer.parseInt(dto.getDocumento());
            } catch (NumberFormatException ignored) {
            }
        }
        return Cliente.builder()
                .documento(docInt)
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .direccion(dto.getDireccion())
                .correo(dto.getCorreo())
                .celular(dto.getCelular())
                .estado("ACTIVO")
                .build();
    }

    public ClienteResponseDTO toDto(Cliente entity) {
        if (entity == null) return null;
        boolean isActivo = entity.getEstado() != null &&
                ("ACTIVO".equalsIgnoreCase(entity.getEstado()) || "A".equalsIgnoreCase(entity.getEstado()) || "true".equalsIgnoreCase(entity.getEstado()));
        return ClienteResponseDTO.builder()
                .id(entity.getIdCliente() != null ? entity.getIdCliente().longValue() : null)
                .documento(entity.getDocumento() != null ? entity.getDocumento().toString() : null)
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .direccion(entity.getDireccion())
                .correo(entity.getCorreo())
                .celular(entity.getCelular())
                .activo(isActivo)
                .build();
    }

    public void updateEntityFromDto(ClienteRequestDTO dto, Cliente entity) {
        if (dto == null || entity == null) return;
        if (dto.getDocumento() != null && !dto.getDocumento().isBlank()) {
            try {
                entity.setDocumento(Integer.parseInt(dto.getDocumento()));
            } catch (NumberFormatException ignored) {
            }
        }
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setDireccion(dto.getDireccion());
        entity.setCorreo(dto.getCorreo());
        entity.setCelular(dto.getCelular());
    }
}
