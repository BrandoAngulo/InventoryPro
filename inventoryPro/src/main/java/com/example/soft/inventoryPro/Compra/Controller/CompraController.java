package com.example.soft.inventoryPro.Compra.Controller;

import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Clientes.Service.Impl.ClienteImpl;
import com.example.soft.inventoryPro.Compra.Models.Compra;
import com.example.soft.inventoryPro.Compra.Service.Impl.DetalleImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Compra")
public class CompraController {

    @Autowired
    private DetalleImpl detalleImpl;

    @PostMapping("/registrarCompra")
    public ResponseEntity<Object> registrarCompra(@RequestBody Compra compra) {
        Compra clienteCompra = detalleImpl.registrarCompra(compra);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCompra);

    }









}
