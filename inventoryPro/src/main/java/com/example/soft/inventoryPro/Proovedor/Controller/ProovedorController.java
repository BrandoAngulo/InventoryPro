package com.example.soft.inventoryPro.Proovedor.Controller;

import com.example.soft.inventoryPro.Compra.Models.Compra;
import com.example.soft.inventoryPro.Compra.Service.Impl.DetalleImpl;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import com.example.soft.inventoryPro.Proovedor.Service.Impl.ProovedorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Proovedor")
public class ProovedorController {

    @Autowired
    private ProovedorImpl proovedorImpl;

    @PostMapping("/registrarProovedor")
    public ResponseEntity<Object> registrarProovedor(@RequestBody Proovedor proovedor) {
        Proovedor proovedorCompra = proovedorImpl.registrarProovedor(proovedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(proovedorCompra);

    }

}
