package com.example.soft.inventoryPro.Clientes.Service;

import com.example.soft.inventoryPro.Clientes.Models.Cliente;

public interface ClienteService {

    Cliente registrarCliente(Cliente cliente);

    Cliente traerClienteId(Integer id);
}
