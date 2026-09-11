package com.example.soft.inventoryPro.Productos.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Long precioVenta;
    private Long costoPromedio;
    private Long categoriaId;
    private String categoriaNombre;
    private Long proveedorId;
    private String proveedorNombre;
    private Integer stockActual;
    private Integer stockMinimo;
    private Boolean activo;
}
