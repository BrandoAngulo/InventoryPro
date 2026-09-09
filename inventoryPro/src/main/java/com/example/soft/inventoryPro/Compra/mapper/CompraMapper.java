package com.example.soft.inventoryPro.Compra.mapper;

import com.example.soft.inventoryPro.Compra.Models.Compra;
import com.example.soft.inventoryPro.Compra.Models.DetalleCompra;
import com.example.soft.inventoryPro.Compra.dto.CompraResponseDTO;
import com.example.soft.inventoryPro.Compra.dto.DetalleCompraResponseDTO;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public class CompraMapper {

    public static CompraResponseDTO toDTO(Compra entity) {
        if (entity == null) return null;
        Long id = entity.getIdCompra() != null ? entity.getIdCompra().longValue() : null;
        Long proveedorId = entity.getProovedor() != null && entity.getProovedor().getIdProovedor() != null ? entity.getProovedor().getIdProovedor().longValue() : null;
        String proveedorNombre = entity.getProovedor() != null ? entity.getProovedor().getNombreProovedor() : null;

        return CompraResponseDTO.builder()
                .id(id)
                .proveedorId(proveedorId)
                .proveedorNombre(proveedorNombre)
                .numeroFacturaProveedor(entity.getNumeroFacturaProveedor())
                .total(entity.getTotal())
                .fecha(entity.getFechaCompra())
                .observacion(entity.getObservacion())
                .detalles(entity.getDetalles() != null ? entity.getDetalles().stream()
                        .map(CompraMapper::toDetalleDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public static DetalleCompraResponseDTO toDetalleDTO(DetalleCompra entity) {
        if (entity == null) return null;
        Long id = entity.getIdDetalleCompra() != null ? entity.getIdDetalleCompra().longValue() : null;
        Long productoId = entity.getProducto() != null && entity.getProducto().getIdProductos() != null ? entity.getProducto().getIdProductos().longValue() : null;
        String productoCodigo = entity.getProducto() != null ? entity.getProducto().getCodigo() : null;
        String productoNombre = entity.getProducto() != null ? entity.getProducto().getNombreProducto() : null;

        return DetalleCompraResponseDTO.builder()
                .id(id)
                .productoId(productoId)
                .productoCodigo(productoCodigo)
                .productoNombre(productoNombre)
                .cantidad(entity.getCantidad())
                .precioUnitario(entity.getPrecioCompra())
                .subtotal(entity.getSubtotal())
                .build();
    }
}
