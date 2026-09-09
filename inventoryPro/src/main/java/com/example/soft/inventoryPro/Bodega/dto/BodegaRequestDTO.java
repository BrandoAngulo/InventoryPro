package com.example.soft.inventoryPro.Bodega.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodegaRequestDTO {

    @NotBlank(message = "El nombre de la bodega es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    private String fechaCreacionBodega;
}
