package com.example.soft.inventoryPro.venta.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaResponseDTO {
    private Integer idVentas;
    private LocalDate fecha;
    private Integer total;
    private Long idCliente;
    private String nombreCliente;
    private Integer idUsuario;
    private String nombreUsuario;
    private List<DetalleVentaResponseDTO> detalles;
}
