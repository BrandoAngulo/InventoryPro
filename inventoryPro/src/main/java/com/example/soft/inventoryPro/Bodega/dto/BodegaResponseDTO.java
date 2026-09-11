package com.example.soft.inventoryPro.Bodega.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodegaResponseDTO {
    private Long idBodega;
    private String nombre;
    private String fechaCreacionBodega;
}
