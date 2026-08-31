package com.example.soft.inventoryPro.Clientes.Controller;

import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Clientes.Service.Impl.ClienteImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Cliente")
public class ClienteController {
    @Autowired
    private ClienteImpl clienteService;

    @PostMapping("/registrarCliente")
    public ResponseEntity<Object> registrarClientes(@RequestBody Cliente cliente) {
        Cliente clienteGuardado = clienteService.registrarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);

    }


    @GetMapping("/traerClienteid/{id}")
    public ResponseEntity<Object> ClientePorId(@PathVariable Integer id) {
        Cliente clienteFindById = clienteService.traerClienteId(id);
        return  ResponseEntity.status(HttpStatus.OK).body(clienteFindById);



    }
}
