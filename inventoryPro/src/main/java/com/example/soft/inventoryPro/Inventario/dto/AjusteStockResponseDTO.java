package com.example.soft.inventoryPro.Inventario.dto;

import com.example.soft.inventoryPro.Inventario.Models.TipoAjuste;
import lombok.*;

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
    private Long costoUnitarioIngreso;
    private Long costoPromedioAnterior;
    private Long costoPromedioResultante;
    private Integer stockAnterior;
    private Integer stockResultante;
    private String motivo;
    private LocalDateTime fecha;
}
