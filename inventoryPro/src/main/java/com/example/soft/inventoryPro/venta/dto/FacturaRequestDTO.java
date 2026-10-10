package com.example.soft.inventoryPro.venta.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @NotBlank(message = "La forma de pago es obligatoria")
    @Pattern(regexp = "EFECTIVO|TARJETA|TRANSFERENCIA", message = "La forma de pago debe ser EFECTIVO, TARJETA o TRANSFERENCIA")
    private String formaPago;

    @Size(max = 255, message = "La observación no debe superar los 255 caracteres")
    private String observacion;

    @NotEmpty(message = "La factura debe contener al menos un detalle de producto")
    @Valid
    private List<DetalleFacturaRequestDTO> detalles;
}
