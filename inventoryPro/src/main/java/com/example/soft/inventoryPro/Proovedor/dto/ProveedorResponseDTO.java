package com.example.soft.inventoryPro.Proovedor.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorResponseDTO {
    private Long id;
    private String documento;
    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;
    private String ciudad;
    private Boolean activo;
}
