package com.example.soft.inventoryPro.Productos.Service.Impl;

import com.example.soft.inventoryPro.Bodega.Models.Bodega;
import com.example.soft.inventoryPro.Bodega.Repository.BodegaRepository;
import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import com.example.soft.inventoryPro.Categoria.Repository.CategoriaRepository;
import com.example.soft.inventoryPro.Productos.Models.ProductoResponse;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Productos.Service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductosImpl implements ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private BodegaRepository bodegaRepository;
    @Override
    public Productos registrarProductos(Productos productos) {
        Productos producto = productoRepository.findByIdProductos(productos.getIdProductos())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado con ID: " + productos.getIdProductos()
                ));
        Set<Categoria> categoriasList = productos.getCategorias().stream()
                .map(modulo -> categoriaRepository.findById(modulo.getIdCategoria()).orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Categoría no encontrada con ID: " + modulo.getIdCategoria()
                        ))
                )
                .collect(Collectors.toSet());

        Set<Bodega> bodegaList = productos.getBodega().stream()
                .map(bodegas -> bodegaRepository.findById(bodegas.getIdBodega()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bodega no esta registrada")
                )).collect(Collectors.toSet());

        try {
            LocalDate fechaIngreso = productos.getFechaIngreso();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            String fechaIngre = fechaIngreso.format(formatter);


            LocalDate nuevaFechaIngreso = LocalDate.parse(fechaIngre, formatter);
            productos.setFechaIngreso(nuevaFechaIngreso);




            productos.setCategorias(categoriasList);
            productos.setBodega(bodegaList);


            return productoRepository.save(productos);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Productos actualizarProductos(Productos productos) {
        Productos actualizarProductos = productoRepository.findById(productos.getIdProductos()).orElseThrow();
        Set<Categoria> categoriasList = productos.getCategorias().stream()
                .map(modulo -> categoriaRepository.findById(modulo.getIdCategoria()).orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Categoría no encontrada con ID: " + modulo.getIdCategoria()
                        ))
                )
                .collect(Collectors.toSet());

        try{
            actualizarProductos.setNombreProducto(productos.getNombreProducto());

            actualizarProductos.setPrecioVenta(productos.getPrecioVenta());
            actualizarProductos.setFechaIngreso(productos.getFechaIngreso());
            actualizarProductos.setObservacion(productos.getObservacion());
            actualizarProductos.setCategorias(categoriasList);



            return productoRepository.save(actualizarProductos);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String eliminarProductos(Integer id) {
        return null;
    }

    @Override
    public Productos findByIdProductos(Integer id) {
        try {
            Productos productos = productoRepository.findById(id).orElseThrow();

            return productos;


        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProductoResponse> findByAllProductos() {
        try{
            List<ProductoResponse> Product = productoRepository.findAllByProductos();
            if (Product.isEmpty()) {

                throw new RuntimeException("La lista esta vacia");
            }
            return Product;
        }catch  (Exception e){
            throw new RuntimeException(e);

        }
    }

}
