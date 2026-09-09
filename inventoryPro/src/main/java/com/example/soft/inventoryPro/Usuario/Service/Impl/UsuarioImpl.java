package com.example.soft.inventoryPro.Usuario.Service.Impl;

import com.example.soft.inventoryPro.Usuario.Models.Usuario;
import com.example.soft.inventoryPro.Usuario.Repository.UsuarioRepository;
import com.example.soft.inventoryPro.Usuario.Service.UsuarioService;
import com.example.soft.inventoryPro.Usuario.dto.UsuarioRequestDTO;
import com.example.soft.inventoryPro.Usuario.dto.UsuarioResponseDTO;
import com.example.soft.inventoryPro.Usuario.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto) {
        Usuario usuario = UsuarioMapper.toEntity(dto);
        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(guardado);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO obtenerPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
        return UsuarioMapper.toDTO(usuario);
    }
}
