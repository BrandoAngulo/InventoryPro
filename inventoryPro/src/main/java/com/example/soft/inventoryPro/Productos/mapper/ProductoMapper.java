package com.example.soft.inventoryPro.Productos.mapper;

import com.example.soft.inventoryPro.Categoria.Models.Categoria;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.dto.ProductoRequestDTO;
import com.example.soft.inventoryPro.Productos.dto.ProductoResponseDTO;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ProductoMapper {

    public Productos toEntity(ProductoRequestDTO dto) {
        if (dto == null) return null;
        Integer stockInit = dto.getStockInicial() != null ? dto.getStockInicial() : 0;

        return Productos.builder()
                .codigo(dto.getCodigo())
                .nombreProducto(dto.getNombre())
                .observacion(dto.getDescripcion())
                .precioVenta(dto.getPrecioVenta())
                .costoPromedio(dto.getCostoInicial())
                .cantidad(stockInit)
                .stockMinimo(dto.getStockMinimo())
                .fechaIngreso(LocalDate.now())
                .activo(true)
                .build();
    }

    public ProductoResponseDTO toDto(Productos entity) {
        if (entity == null) return null;

        Long catId = null;
        String catNombre = null;
        if (entity.getCategorias() != null && !entity.getCategorias().isEmpty()) {
            Categoria cat = entity.getCategorias().iterator().next();
            catId = cat.getIdCategoria();
            catNombre = cat.getNombreCategoria();
        }

        Long provId = null;
        String provNombre = null;
        if (entity.getProveedores() != null && !entity.getProveedores().isEmpty()) {
            Proovedor prov = entity.getProveedores().iterator().next();
            provId = prov.getIdProovedor();
            provNombre = prov.getNombreProovedor();
        }

        return ProductoResponseDTO.builder()
                .id(entity.getIdProductos())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombreProducto())
                .descripcion(entity.getObservacion())
                .precioVenta(entity.getPrecioVenta())
                .costoPromedio(entity.getCostoPromedio())
                .categoriaId(catId)
                .categoriaNombre(catNombre)
                .proveedorId(provId)
                .proveedorNombre(provNombre)
                .stockActual(entity.getCantidad())
                .stockMinimo(entity.getStockMinimo())
                .activo(entity.getActivo() != null ? entity.getActivo() : true)
                .build();
    }

    public void updateEntityFromDto(ProductoRequestDTO dto, Productos entity) {
        if (dto == null || entity == null) return;
        entity.setCodigo(dto.getCodigo());
        entity.setNombreProducto(dto.getNombre());
        entity.setObservacion(dto.getDescripcion());
        if (dto.getPrecioVenta() != null) {
            entity.setPrecioVenta(dto.getPrecioVenta());
        }
        if (dto.getStockMinimo() != null) {
            entity.setStockMinimo(dto.getStockMinimo());
        }
    }
}
