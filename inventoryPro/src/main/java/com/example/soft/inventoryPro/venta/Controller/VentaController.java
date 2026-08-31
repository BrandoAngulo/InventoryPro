package com.example.soft.inventoryPro.venta.Controller;

import com.example.soft.inventoryPro.venta.Models.Ventas;
import com.example.soft.inventoryPro.venta.Service.Impl.VentaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ventas")
public class VentaController {
    @Autowired
    private VentaImpl ventaImpl;

    @PostMapping("/registrarVenta")
    public ResponseEntity<Object> registrarVenta(@RequestBody Ventas ventas) {
        Ventas vs =  ventaImpl.registrarVentas(ventas);
        return ResponseEntity.status(HttpStatus.OK).body(vs);
    }



    @GetMapping("/listarVentas")
    public ResponseEntity<Object> listarTodasVentas() {
        List<Ventas> vs =  ventaImpl.listarTodasVentas();
        return ResponseEntity.status(HttpStatus.OK).body(vs);
    }
}
