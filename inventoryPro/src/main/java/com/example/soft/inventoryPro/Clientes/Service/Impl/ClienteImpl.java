package com.example.soft.inventoryPro.Clientes.Service.Impl;

import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Clientes.Repository.ClienteRepository;
import com.example.soft.inventoryPro.Clientes.Service.ClienteService;
import com.example.soft.inventoryPro.Usuario.Models.Usuario;
import com.example.soft.inventoryPro.Usuario.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class ClienteImpl implements ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Override
    public Cliente registrarCliente(Cliente cliente) {
        Usuario usuario = usuarioRepository.findById(cliente.getUsuario().getIdUsuario()).orElseThrow(
                (() -> new RuntimeException("Usuario no encontrado con ID: " + cliente.getUsuario().getIdUsuario())));





        clienteRepository.findClienteByDocumento(cliente.getDocumento()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente no existe"));

        // throw new RequestException("Documento del cliente no exite", HttpStatus.BAD_REQUEST.value());
        try {

            cliente.setEstado("A");
            cliente.setUsuario(usuario);
            return clienteRepository.save(cliente);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cliente traerClienteId(Integer id) {
        Cliente cliente = clienteRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente no existe"));

        try{
            return cliente;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
