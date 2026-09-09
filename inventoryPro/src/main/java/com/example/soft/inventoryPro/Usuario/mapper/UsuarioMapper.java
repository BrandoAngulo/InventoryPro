package com.example.soft.inventoryPro.Usuario.mapper;

import com.example.soft.inventoryPro.Usuario.Models.Usuario;
import com.example.soft.inventoryPro.Usuario.dto.UsuarioRequestDTO;
import com.example.soft.inventoryPro.Usuario.dto.UsuarioResponseDTO;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null;
        return Usuario.builder()
                .documento(dto.getDocumento())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .direccion(dto.getDireccion())
                .correo(dto.getCorreo())
                .celular(dto.getCelular())
                .estado(dto.getEstado() != null ? dto.getEstado() : "ACTIVO")
                .build();
    }

    public static UsuarioResponseDTO toDTO(Usuario entity) {
        if (entity == null) return null;
        return UsuarioResponseDTO.builder()
                .idUsuario(entity.getIdUsuario())
                .documento(entity.getDocumento())
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .direccion(entity.getDireccion())
                .correo(entity.getCorreo())
                .celular(entity.getCelular())
                .estado(entity.getEstado())
                .build();
    }
}
