package com.example.soft.inventoryPro.venta.mapper;

import com.example.soft.inventoryPro.venta.Models.DetalleVenta;
import com.example.soft.inventoryPro.venta.Models.Ventas;
import com.example.soft.inventoryPro.venta.dto.DetalleVentaResponseDTO;
import com.example.soft.inventoryPro.venta.dto.VentaResponseDTO;

import java.util.stream.Collectors;

public class VentaMapper {

    public static VentaResponseDTO toDTO(Ventas entity) {
        if (entity == null) return null;
        return VentaResponseDTO.builder()
                .idVentas(entity.getId())
                .fecha(entity.getFecha())
                .total(entity.getTotal())
                .idCliente(entity.getClientes() != null && entity.getClientes().getIdCliente() != null ? entity.getClientes().getIdCliente().longValue() : null)
                .nombreCliente(entity.getClientes() != null ? entity.getClientes().getNombre() : null)
                .idUsuario(entity.getUsuario() != null ? entity.getUsuario().getIdUsuario() : null)
                .nombreUsuario(entity.getUsuario() != null ? entity.getUsuario().getNombre() : null)
                .detalles(entity.getDetalles() != null ? entity.getDetalles().stream()
                        .map(VentaMapper::toDetalleDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public static DetalleVentaResponseDTO toDetalleDTO(DetalleVenta entity) {
        if (entity == null) return null;
        return DetalleVentaResponseDTO.builder()
                .idDetalleVenta(entity.getId())
                .idProducto(entity.getProductos() != null && entity.getProductos().getIdProductos() != null ? entity.getProductos().getIdProductos().longValue() : null)
                .nombreProducto(entity.getProductos() != null ? entity.getProductos().getNombreProducto() : null)
                .cantidad(entity.getCantidad())
                .precioUnitario(entity.getPrecioUnitario())
                .build();
    }
}
