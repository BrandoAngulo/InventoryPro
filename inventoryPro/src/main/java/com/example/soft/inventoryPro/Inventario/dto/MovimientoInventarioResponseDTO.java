package com.example.soft.inventoryPro.Inventario.dto;

import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventarioResponseDTO {
    private Long id;
    private Long productoId;
    private String productoCodigo;
    private String productoNombre;
    private TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal costoUnitario;
    private LocalDateTime fecha;
    private Long referenciaId;
    private String observacion;
}
