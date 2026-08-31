package com.example.soft.inventoryPro.Proovedor.Service.Impl;

import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import com.example.soft.inventoryPro.Proovedor.Repository.ProovedorRepository;
import com.example.soft.inventoryPro.Proovedor.Service.ProovedorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class ProovedorImpl implements ProovedorService {

       @Autowired
    private ProovedorRepository proovedorRepository;


    @Override
    public Proovedor registrarProovedor(Proovedor proovedor) {
        try {

            return proovedorRepository.save(proovedor);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Proovedor actualizarProovedor(Proovedor proovedor) {
        Proovedor actualizarProovedor = proovedorRepository.findById(proovedor.getIdProovedor()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proovedor no encontrada con ID: " + proovedor.getIdProovedor()));

        try{
            actualizarProovedor.setNombreProovedor(proovedor.getNombreProovedor());
            actualizarProovedor.setDocumento(proovedor.getDocumento());
            actualizarProovedor.setTelefono(proovedor.getTelefono());
            actualizarProovedor.setCorreo(proovedor.getCorreo());
            actualizarProovedor.setCiudad(proovedor.getCiudad());
            actualizarProovedor.setEstado(proovedor.getEstado());



            return proovedorRepository.save(actualizarProovedor);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String eliminarProovedor(Integer id) {
        return "";
    }

    @Override
    public Proovedor findByIdProovedor(Integer id) {
        try{
            return proovedorRepository.findById(id)  .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Prooveedr no encontrado con ID: " + id
            ));

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Proovedor> findByAllProovedor() {
        List<Proovedor> proovedorList = proovedorRepository.findAll();

        try{
            return proovedorList;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
