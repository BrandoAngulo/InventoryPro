package com.example.soft.inventoryPro.Compra.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraResponseDTO {
    private Long id;
    private Long proveedorId;
    private String proveedorNombre;
    private String numeroFacturaProveedor;
    private BigDecimal total;
    private LocalDateTime fecha;
    private String observacion;
    private List<DetalleCompraResponseDTO> detalles;

    public Long getIdCompra() {
        return id;
    }

    public Long getIdProveedor() {
        return proveedorId;
    }

    public String getNombreProveedor() {
        return proveedorNombre;
    }
}
