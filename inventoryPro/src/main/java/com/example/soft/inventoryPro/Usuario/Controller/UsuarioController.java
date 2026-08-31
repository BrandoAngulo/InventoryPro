package com.example.soft.inventoryPro.Usuario.Controller;

import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import com.example.soft.inventoryPro.Usuario.Models.Usuario;
import com.example.soft.inventoryPro.Usuario.Service.Impl.UsuarioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioImpl usuarioService;

    @PostMapping("/registrarUsuario")
    public ResponseEntity<Object> registrarUsuario(@RequestBody Usuario usuario){

          Usuario registrarUsuario = usuarioService.registrarUsuario(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrarUsuario);
    }
}
