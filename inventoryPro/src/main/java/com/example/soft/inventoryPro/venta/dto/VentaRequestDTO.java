package com.example.soft.inventoryPro.venta.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUsuario;

    @NotEmpty(message = "La venta debe incluir al menos un producto")
    @Valid
    private List<DetalleVentaRequestDTO> detalles;
}
