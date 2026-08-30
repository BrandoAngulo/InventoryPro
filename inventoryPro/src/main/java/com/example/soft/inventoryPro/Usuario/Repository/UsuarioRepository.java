package com.example.soft.inventoryPro.Usuario.Repository;

import com.example.soft.inventoryPro.Usuario.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository  extends JpaRepository<Usuario, Integer> {
}
