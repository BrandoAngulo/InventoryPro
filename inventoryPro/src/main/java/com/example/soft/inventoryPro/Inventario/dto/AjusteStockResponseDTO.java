package com.example.soft.inventoryPro.Inventario.dto;

import com.example.soft.inventoryPro.Inventario.Models.TipoAjuste;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AjusteStockResponseDTO {
    private Long id;
    private Long productoId;
    private String productoCodigo;
    private String productoNombre;
    private TipoAjuste tipoAjuste;
    private Integer cantidad;
    private BigDecimal costoUnitarioIngreso;
    private BigDecimal costoPromedioAnterior;
    private BigDecimal costoPromedioResultante;
    private Integer stockAnterior;
    private Integer stockResultante;
    private String motivo;
    private LocalDateTime fecha;
}
