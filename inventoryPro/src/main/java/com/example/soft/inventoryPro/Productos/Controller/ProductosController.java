package com.example.soft.inventoryPro.Productos.Controller;

import com.example.soft.inventoryPro.Productos.Models.ProductoResponse;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Service.Impl.ProductosImpl;
import com.example.soft.inventoryPro.Productos.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductosController {


    @Autowired
    private ProductosImpl productosImpl;
    @PostMapping("/registrarProductos")
    public ResponseEntity<Object> registrarProducto(@RequestBody Productos productos){

        Productos productoGuardado = productosImpl.registrarProductos(productos);
        System.out.println("productoGuardado" + productoGuardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);

    }

    @PutMapping("/actualizar")
    public ResponseEntity<Object> actualizarProducto(@RequestBody Productos productos){

        Productos productoGuardado = productosImpl.actualizarProductos(productos);

        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);

    }


    @GetMapping("/traerporId/{id}")
    public ResponseEntity<Object> findByIdProducto(@PathVariable Integer id){
        Productos productosFindById = productosImpl.findByIdProductos(id);
        return  ResponseEntity.status(HttpStatus.OK).body(productosFindById);

    }

    @GetMapping("/traerporAll")
    public ResponseEntity<Object> traerProducto(){
        List<ProductoResponse> productosFindByAll = productosImpl.findByAllProductos();
        return  ResponseEntity.status(HttpStatus.OK).body(productosFindByAll);

    }
}
