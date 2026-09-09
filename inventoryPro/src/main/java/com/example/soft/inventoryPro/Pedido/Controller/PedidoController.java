package com.example.soft.inventoryPro.Pedido.Controller;

import com.example.soft.inventoryPro.Pedido.Models.EstadoPedido;
import com.example.soft.inventoryPro.Pedido.Service.PedidoService;
import com.example.soft.inventoryPro.Pedido.dto.*;
import com.example.soft.inventoryPro.venta.dto.FacturaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido(@Valid @RequestBody PedidoRequestDTO dto) {
        PedidoResponseDTO response = pedidoService.crearPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos(
            @RequestParam(required = false) EstadoPedido estado,
            @RequestParam(required = false) Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPedidos(estado, clienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtenerPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> editarPedido(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.ok(pedidoService.editarPedido(id, dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoPedidoDTO dto) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, dto));
    }

    @PostMapping("/{id}/facturar")
    public ResponseEntity<FacturaResponseDTO> facturarPedido(
            @PathVariable Long id,
            @Valid @RequestBody FacturarPedidoRequestDTO dto) {
        FacturaResponseDTO response = pedidoService.facturarPedido(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
