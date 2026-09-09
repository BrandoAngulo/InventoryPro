package com.example.soft.inventoryPro.Clientes.Controller;

import com.example.soft.inventoryPro.Clientes.Service.ClienteService;
import com.example.soft.inventoryPro.Clientes.dto.ClienteRequestDTO;
import com.example.soft.inventoryPro.Clientes.dto.ClienteResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO clienteGuardado = clienteService.registrarCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes() {
        List<ClienteResponseDTO> clientes = clienteService.obtenerTodos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> clientePorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.traerClienteId(id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO clienteActualizado = clienteService.actualizarCliente(id, dto);
        return ResponseEntity.ok(clienteActualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ClienteResponseDTO> cambiarEstado(@PathVariable Long id) {
        ClienteResponseDTO clienteActualizado = clienteService.cambiarEstado(id);
        return ResponseEntity.ok(clienteActualizado);
    }
}
