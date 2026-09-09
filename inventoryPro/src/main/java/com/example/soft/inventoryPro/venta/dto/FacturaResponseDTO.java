package com.example.soft.inventoryPro.venta.dto;

import lombok.*;
import java.math.BigDecimal;
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
    private BigDecimal subtotal;
    private BigDecimal impuesto;
    private BigDecimal total;
    private String formaPago;
    private String estado;
    private LocalDateTime fecha;
    private String observacion;
    private List<DetalleFacturaResponseDTO> detalles;
}
