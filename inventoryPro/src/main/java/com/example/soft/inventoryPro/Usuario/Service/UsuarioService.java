package com.example.soft.inventoryPro.Usuario.Service;

import com.example.soft.inventoryPro.Usuario.dto.UsuarioRequestDTO;
import com.example.soft.inventoryPro.Usuario.dto.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto);
    List<UsuarioResponseDTO> listarUsuarios();
    UsuarioResponseDTO obtenerPorId(Integer id);
}
