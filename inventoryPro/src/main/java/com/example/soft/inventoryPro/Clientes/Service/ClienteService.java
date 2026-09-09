package com.example.soft.inventoryPro.Clientes.Service;

import com.example.soft.inventoryPro.Clientes.dto.ClienteRequestDTO;
import com.example.soft.inventoryPro.Clientes.dto.ClienteResponseDTO;

import java.util.List;

public interface ClienteService {
    ClienteResponseDTO registrarCliente(ClienteRequestDTO dto);
    List<ClienteResponseDTO> obtenerTodos();
    ClienteResponseDTO traerClienteId(Long id);
    ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO dto);
    ClienteResponseDTO cambiarEstado(Long id);
}
