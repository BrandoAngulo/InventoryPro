package com.example.soft.inventoryPro.Compra.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraRequestDTO {

    @NotNull(message = "El ID del proveedor es obligatorio")
    private Long idProveedor;

    private Long proveedorId;

    @Size(max = 50, message = "El número de factura de proveedor no debe superar 50 caracteres")
    private String numeroFacturaProveedor;

    @Size(max = 255, message = "La observación no debe superar 255 caracteres")
    private String observacion;

    @NotEmpty(message = "La compra debe contener al menos un detalle de producto")
    @Valid
    private List<DetalleCompraRequestDTO> detalles;

    public Long getProveedorId() {
        return proveedorId != null ? proveedorId : idProveedor;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
        if (this.idProveedor == null) {
            this.idProveedor = proveedorId;
        }
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
        if (this.proveedorId == null) {
            this.proveedorId = idProveedor;
        }
    }
}
