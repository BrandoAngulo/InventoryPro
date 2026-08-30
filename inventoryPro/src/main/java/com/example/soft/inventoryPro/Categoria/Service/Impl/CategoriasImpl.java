package com.example.soft.inventoryPro.Categoria.Service.Impl;

import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import com.example.soft.inventoryPro.Categoria.Repository.CategoriaRepository;
import com.example.soft.inventoryPro.Categoria.Service.CategoriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Slf4j
@Service
public class CategoriasImpl implements CategoriaService {



    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public Categoria registrarCategoria(Categoria categoria) {
        try {

            return categoriaRepository.save(categoria);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Categoria actualizarCategoria(Categoria categoria) {
        Categoria actualizarCategorias = categoriaRepository.findById(categoria.getIdCategoria()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada con ID: " + categoria.getIdCategoria()));

        try{
            actualizarCategorias.setNombreCategoria(categoria.getNombreCategoria());

            return categoriaRepository.save(actualizarCategorias);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String eliminarCategoria(Integer id) {
        return null;
    }

    @Override
    public Categoria findByIdCategoria(Integer id) {
        try{
            return categoriaRepository.findById(id).orElseThrow();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Categoria> traerTodoCategoria() {
        List<Categoria> categoriaList = categoriaRepository.findAll();

        try{
            return categoriaList;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
