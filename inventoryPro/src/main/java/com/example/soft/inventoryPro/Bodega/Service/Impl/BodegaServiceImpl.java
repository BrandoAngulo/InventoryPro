package com.example.soft.inventoryPro.Bodega.Service.Impl;

import com.example.soft.inventoryPro.Bodega.Models.Bodega;
import com.example.soft.inventoryPro.Bodega.Repository.BodegaRepository;
import com.example.soft.inventoryPro.Bodega.Service.BodegaService;
import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Slf4j
@Service
public class BodegaServiceImpl  implements BodegaService {
    @Autowired
    BodegaRepository bodegaRepository;
    @Override
    public Bodega registrarBodega(Bodega bodega) {
        try {

            return bodegaRepository.save(bodega);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Bodega> traerTodasBoderga() {
        try {
            List<Bodega> bodegas = bodegaRepository.findAll();


            return bodegas;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bodega actualizarBodega(Bodega bodega) {
        Bodega actualizarBodega = bodegaRepository.findById(bodega.getIdBodega()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bodega no encontrada con ID: " + bodega.getIdBodega()));

        try{
            actualizarBodega.setNombre(bodega.getNombre());

            return bodegaRepository.save(actualizarBodega);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bodega traerIdBoderga(Integer id) {
        try {

            Bodega bodegaId = bodegaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bodega no encontrada con ID: " + id));

            return  bodegaId;

        } catch(RuntimeException ex) {
            log.error("traerBodega: ".concat(ex.getMessage())
            );
            throw new RuntimeException(ex);
        }
    }
}
