package com.example.soft.inventoryPro.Categoria.Service;

import com.example.soft.inventoryPro.Categoria.Models.Categoria;

import java.util.List;

public interface CategoriaService {

    Categoria registrarCategoria(Categoria categoria);

    Categoria actualizarCategoria(Categoria categoria);

    String eliminarCategoria(Integer id);

    Categoria findByIdCategoria(Integer id);

    List<Categoria> traerTodoCategoria();

}
