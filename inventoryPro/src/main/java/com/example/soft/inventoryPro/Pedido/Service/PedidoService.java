package com.example.soft.inventoryPro.Pedido.Service;

import com.example.soft.inventoryPro.Pedido.Models.EstadoPedido;
import com.example.soft.inventoryPro.Pedido.dto.*;
import com.example.soft.inventoryPro.venta.dto.FacturaResponseDTO;

import java.util.List;

public interface PedidoService {
    PedidoResponseDTO crearPedido(PedidoRequestDTO dto);
    List<PedidoResponseDTO> listarPedidos(EstadoPedido estado, Long clienteId);
    PedidoResponseDTO obtenerPedidoPorId(Long id);
    PedidoResponseDTO editarPedido(Long id, PedidoRequestDTO dto);
    PedidoResponseDTO cambiarEstado(Long id, CambioEstadoPedidoDTO dto);
    FacturaResponseDTO facturarPedido(Long id, FacturarPedidoRequestDTO dto);
}
