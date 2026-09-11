package com.example.soft.inventoryPro.venta.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaResponseDTO {
    private Long id;
    private String numeroFactura;
    private Long clienteId;
    private String clienteNombre;
    private String clienteDocumento;
    private Long subtotal;
    private Long impuesto;
    private Long total;
    private String formaPago;
    private String estado;
    private LocalDateTime fecha;
    private String observacion;
    private List<DetalleFacturaResponseDTO> detalles;
}
