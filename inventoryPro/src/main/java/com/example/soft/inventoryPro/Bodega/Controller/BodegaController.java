package com.example.soft.inventoryPro.Bodega.Controller;

import com.example.soft.inventoryPro.Bodega.Models.Bodega;
import com.example.soft.inventoryPro.Bodega.Service.Impl.BodegaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Bodega")
public class BodegaController {
    @Autowired
    BodegaServiceImpl bodegaService;
    @PostMapping("/registrarBodega")

    public ResponseEntity<Object> registrarBodega(@RequestBody Bodega bodega) {
        Bodega bodegaRegistrada = bodegaService.registrarBodega(bodega);

        return   ResponseEntity.status(HttpStatus.OK).body(bodegaRegistrada);
    }


    @GetMapping("/listarBodegas")
    public ResponseEntity<Object> listarBodegas() {

        List<Bodega> traetBodegas = bodegaService.traerTodasBoderga();

        return   ResponseEntity.status(HttpStatus.OK).body(traetBodegas);


    }

    @GetMapping("/listarBodega/{id}")
    public ResponseEntity<Object> listarBodega(@PathVariable Integer id) {

        Bodega bodegaFindById = bodegaService.traerIdBoderga(id);
        return  ResponseEntity.status(HttpStatus.OK).body(bodegaFindById);


    }
}
