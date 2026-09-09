package com.example.soft.inventoryPro.Clientes.Service.Impl;

import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Clientes.Repository.ClienteRepository;
import com.example.soft.inventoryPro.Clientes.Service.ClienteService;
import com.example.soft.inventoryPro.Clientes.dto.ClienteRequestDTO;
import com.example.soft.inventoryPro.Clientes.dto.ClienteResponseDTO;
import com.example.soft.inventoryPro.Clientes.mapper.ClienteMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClienteImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    @Transactional
    public ClienteResponseDTO registrarCliente(ClienteRequestDTO dto) {
        if (dto.getDocumento() != null && !dto.getDocumento().isBlank()) {
            try {
                Integer docInt = Integer.parseInt(dto.getDocumento());
                clienteRepository.findClienteByDocumento(docInt).ifPresent(c -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El cliente ya existe con el documento: " + dto.getDocumento());
                });
            } catch (NumberFormatException ignored) {
            }
        }

        Cliente cliente = clienteMapper.toEntity(dto);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return clienteMapper.toDto(clienteGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> obtenerTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO traerClienteId(Long id) {
        Cliente cliente = clienteRepository.findById(id.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente no existe con ID: " + id));
        return clienteMapper.toDto(cliente);
    }

    @Override
    @Transactional
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente no existe con ID: " + id));
        clienteMapper.updateEntityFromDto(dto, cliente);
        Cliente actualizado = clienteRepository.save(cliente);
        return clienteMapper.toDto(actualizado);
    }

    @Override
    @Transactional
    public ClienteResponseDTO cambiarEstado(Long id) {
        Cliente cliente = clienteRepository.findById(id.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente no existe con ID: " + id));

        boolean esActivo = cliente.getEstado() != null &&
                ("ACTIVO".equalsIgnoreCase(cliente.getEstado()) || "A".equalsIgnoreCase(cliente.getEstado()));
        cliente.setEstado(esActivo ? "INACTIVO" : "ACTIVO");
        Cliente actualizado = clienteRepository.save(cliente);
        return clienteMapper.toDto(actualizado);
    }
}
