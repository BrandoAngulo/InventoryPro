package com.example.soft.inventoryPro.venta.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaResponseDTO {
    private Long idVentas;
    private LocalDate fecha;
    private Long total;
    private Long idCliente;
    private String nombreCliente;
    private Long idUsuario;
    private String nombreUsuario;
    private List<DetalleVentaResponseDTO> detalles;
}
