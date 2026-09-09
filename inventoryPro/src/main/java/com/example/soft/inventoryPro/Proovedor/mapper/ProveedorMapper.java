package com.example.soft.inventoryPro.Proovedor.mapper;

import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import com.example.soft.inventoryPro.Proovedor.dto.ProveedorRequestDTO;
import com.example.soft.inventoryPro.Proovedor.dto.ProveedorResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProveedorMapper {

    public Proovedor toEntity(ProveedorRequestDTO dto) {
        if (dto == null) return null;
        return Proovedor.builder()
                .documento(dto.getDocumento())
                .nombreProovedor(dto.getNombre())
                .telefono(dto.getTelefono())
                .correo(dto.getCorreo())
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .estado(true)
                .build();
    }

    public ProveedorResponseDTO toDto(Proovedor entity) {
        if (entity == null) return null;
        return ProveedorResponseDTO.builder()
                .id(entity.getIdProovedor() != null ? entity.getIdProovedor().longValue() : null)
                .documento(entity.getDocumento())
                .nombre(entity.getNombreProovedor())
                .telefono(entity.getTelefono())
                .correo(entity.getCorreo())
                .direccion(entity.getDireccion())
                .ciudad(entity.getCiudad())
                .activo(entity.getEstado() != null ? entity.getEstado() : true)
                .build();
    }

    public void updateEntityFromDto(ProveedorRequestDTO dto, Proovedor entity) {
        if (dto == null || entity == null) return;
        entity.setDocumento(dto.getDocumento());
        entity.setNombreProovedor(dto.getNombre());
        entity.setTelefono(dto.getTelefono());
        entity.setCorreo(dto.getCorreo());
        entity.setDireccion(dto.getDireccion());
        entity.setCiudad(dto.getCiudad());
    }
}
