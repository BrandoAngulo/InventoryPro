package com.example.soft.inventoryPro.Clientes.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String documento;
    private String nombre;
    private String apellido;
    private String direccion;
    private String correo;
    private String celular;
    private Boolean activo;
}
