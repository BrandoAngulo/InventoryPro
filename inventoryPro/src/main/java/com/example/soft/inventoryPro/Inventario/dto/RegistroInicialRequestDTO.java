package com.example.soft.inventoryPro.Inventario.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroInicialRequestDTO {

    @Size(max = 255, message = "La observación no debe superar 255 caracteres")
    private String observacion;

    @NotEmpty(message = "Debe enviar al menos un ítem para carga inicial")
    @Valid
    private List<DetalleRegistroInicialRequestDTO> items;
}
