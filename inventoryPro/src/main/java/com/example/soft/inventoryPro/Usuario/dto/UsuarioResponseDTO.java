package com.example.soft.inventoryPro.Usuario.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long idUsuario;
    private Integer documento;
    private String nombre;
    private String apellido;
    private String direccion;
    private String correo;
    private String celular;
    private String estado;
}
