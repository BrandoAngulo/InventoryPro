package com.example.soft.inventoryPro.Productos.Service.Impl;

import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import com.example.soft.inventoryPro.Categoria.Repository.CategoriaRepository;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Productos.Service.ProductoService;
import com.example.soft.inventoryPro.Productos.dto.ProductoRequestDTO;
import com.example.soft.inventoryPro.Productos.dto.ProductoResponseDTO;
import com.example.soft.inventoryPro.Productos.mapper.ProductoMapper;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import com.example.soft.inventoryPro.Proovedor.Repository.ProovedorRepository;
import com.example.soft.inventoryPro.common.exception.BusinessRuleException;
import com.example.soft.inventoryPro.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductosImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProovedorRepository proovedorRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional
    public ProductoResponseDTO registrarProductos(ProductoRequestDTO dto) {
        if (productoRepository.existsByCodigo(dto.getCodigo())) {
            throw new BusinessRuleException("Ya existe un producto con el código: " + dto.getCodigo());
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

        Proovedor proveedor = proovedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + dto.getProveedorId()));

        Productos producto = productoMapper.toEntity(dto);

        Set<Categoria> catSet = new HashSet<>();
        catSet.add(categoria);
        producto.setCategorias(catSet);

        Set<Proovedor> provSet = new HashSet<>();
        provSet.add(proveedor);
        producto.setProveedores(provSet);

        Productos guardado = productoRepository.save(producto);
        return productoMapper.toDto(guardado);
    }

    @Override
    @Transactional
    public ProductoResponseDTO actualizarProductos(Long id, ProductoRequestDTO dto) {
        Productos producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (dto.getCodigo() != null && productoRepository.existsByCodigoAndIdProductosNot(dto.getCodigo(), id)) {
            throw new BusinessRuleException("Ya existe otro producto con el código: " + dto.getCodigo());
        }

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + dto.getCategoriaId()));
            Set<Categoria> catSet = new HashSet<>();
            catSet.add(categoria);
            producto.setCategorias(catSet);
        }

        if (dto.getProveedorId() != null) {
            Proovedor proveedor = proovedorRepository.findById(dto.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + dto.getProveedorId()));
            Set<Proovedor> provSet = new HashSet<>();
            provSet.add(proveedor);
            producto.setProveedores(provSet);
        }

        productoMapper.updateEntityFromDto(dto, producto);
        Productos actualizado = productoRepository.save(producto);
        return productoMapper.toDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminarProductos(Long id) {
        Productos producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO findByIdProductos(Long id) {
        Productos producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return productoMapper.toDto(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByAllProductos() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductoResponseDTO cambiarEstado(Long id) {
        Productos producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        boolean actual = producto.getActivo() != null ? producto.getActivo() : true;
        producto.setActivo(!actual);
        Productos actualizado = productoRepository.save(producto);
        return productoMapper.toDto(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductosBajoStock() {
        return productoRepository.findProductosBajoStock()
                .stream()
                .map(productoMapper::toDto)
                .toList();
    }
}
