package com.example.soft.inventoryPro.Proovedor.Service.Impl;

import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import com.example.soft.inventoryPro.Proovedor.Repository.ProovedorRepository;
import com.example.soft.inventoryPro.Proovedor.Service.ProovedorService;
import com.example.soft.inventoryPro.Proovedor.dto.ProveedorRequestDTO;
import com.example.soft.inventoryPro.Proovedor.dto.ProveedorResponseDTO;
import com.example.soft.inventoryPro.Proovedor.mapper.ProveedorMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProovedorImpl implements ProovedorService {

    private final ProovedorRepository proovedorRepository;
    private final ProveedorMapper proveedorMapper;

    @Override
    @Transactional
    public ProveedorResponseDTO registrarProovedor(ProveedorRequestDTO dto) {
        if (dto.getDocumento() != null && !dto.getDocumento().isBlank()) {
            if (proovedorRepository.existsByDocumento(dto.getDocumento())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un proveedor registrado con el documento/NIT: " + dto.getDocumento());
            }
        }
        Proovedor proovedor = proveedorMapper.toEntity(dto);
        Proovedor guardado = proovedorRepository.save(proovedor);
        return proveedorMapper.toDto(guardado);
    }

    @Override
    @Transactional
    public ProveedorResponseDTO actualizarProovedor(Long id, ProveedorRequestDTO dto) {
        Proovedor proovedor = proovedorRepository.findById(id.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con ID: " + id));

        if (dto.getDocumento() != null && !dto.getDocumento().isBlank()) {
            if (proovedorRepository.existsByDocumentoAndIdProovedorNot(dto.getDocumento(), id.intValue())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe otro proveedor registrado con el documento/NIT: " + dto.getDocumento());
            }
        }

        proveedorMapper.updateEntityFromDto(dto, proovedor);
        Proovedor actualizado = proovedorRepository.save(proovedor);
        return proveedorMapper.toDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminarProovedor(Long id) {
        Proovedor proovedor = proovedorRepository.findById(id.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con ID: " + id));
        proovedor.setEstado(false);
        proovedorRepository.save(proovedor);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponseDTO findByIdProovedor(Long id) {
        Proovedor proovedor = proovedorRepository.findById(id.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con ID: " + id));
        return proveedorMapper.toDto(proovedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> findByAllProovedor() {
        return proovedorRepository.findAll()
                .stream()
                .map(proveedorMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProveedorResponseDTO cambiarEstado(Long id) {
        Proovedor proovedor = proovedorRepository.findById(id.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con ID: " + id));

        boolean actual = proovedor.getEstado() != null ? proovedor.getEstado() : true;
        proovedor.setEstado(!actual);
        Proovedor actualizado = proovedorRepository.save(proovedor);
        return proveedorMapper.toDto(actualizado);
    }
}
