package com.example.soft.inventoryPro.Pedido.dto;

import com.example.soft.inventoryPro.Pedido.Models.EstadoPedido;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private Long id;
    private String numeroPedido;
    private Long clienteId;
    private String clienteNombre;
    private String clienteDocumento;
    private EstadoPedido estado;
    private BigDecimal subtotal;
    private BigDecimal descuentoGeneral;
    private BigDecimal impuesto;
    private BigDecimal total;
    private String observacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<DetallePedidoResponseDTO> detalles;
}
