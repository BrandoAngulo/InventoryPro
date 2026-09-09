package com.example.soft.inventoryPro.venta.Service.Impl;

import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import com.example.soft.inventoryPro.Clientes.Repository.ClienteRepository;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Stock.Models.Stock;
import com.example.soft.inventoryPro.Stock.Repository.StockRepository;
import com.example.soft.inventoryPro.Usuario.Models.Usuario;
import com.example.soft.inventoryPro.Usuario.Repository.UsuarioRepository;
import com.example.soft.inventoryPro.venta.Models.DetalleVenta;
import com.example.soft.inventoryPro.venta.Models.Ventas;
import com.example.soft.inventoryPro.venta.Repository.VentaRepository;
import com.example.soft.inventoryPro.venta.Service.VentaService;
import com.example.soft.inventoryPro.venta.dto.DetalleVentaRequestDTO;
import com.example.soft.inventoryPro.venta.dto.VentaRequestDTO;
import com.example.soft.inventoryPro.venta.dto.VentaResponseDTO;
import com.example.soft.inventoryPro.venta.mapper.VentaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VentaImpl implements VentaService {

    private final StockRepository stockRepository;
    private final ProductoRepository productosRepository;
    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public VentaResponseDTO registrarVentas(VentaRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdCliente().intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + dto.getIdCliente()));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + dto.getIdUsuario()));

        Ventas venta = new Ventas();
        venta.setFecha(LocalDate.now());
        venta.setClientes(cliente);
        venta.setUsuario(usuario);

        int total = 0;
        List<DetalleVenta> detallesVenta = new ArrayList<>();

        for (DetalleVentaRequestDTO detalleDTO : dto.getDetalles()) {
            Productos producto = productosRepository.findById(detalleDTO.getIdProducto().intValue())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + detalleDTO.getIdProducto()));

            Stock stock = stockRepository.findByProductos_IdProductos(producto.getIdProductos())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock no encontrado para el producto: " + producto.getNombreProducto()));

            if (stock.getCantidadStock() < detalleDTO.getCantidad()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuficiente para el producto: " + producto.getNombreProducto());
            }

            stock.setCantidadStock(stock.getCantidadStock() - detalleDTO.getCantidad());
            stockRepository.save(stock);

            int precioUnitario = producto.getPrecioVenta() != null ? producto.getPrecioVenta() : 0;
            int subtotal = precioUnitario * detalleDTO.getCantidad();
            total += subtotal;

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVentas(venta);
            detalle.setProductos(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detallesVenta.add(detalle);
        }

        venta.setTotal(total);
        venta.setDetalles(detallesVenta);

        Ventas ventaRegistrada = ventaRepository.save(venta);
        return VentaMapper.toDTO(ventaRegistrada);
    }

    @Override
    public VentaResponseDTO listarVentas(Integer idVentas) {
        Ventas venta = ventaRepository.findById(idVentas)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada con ID: " + idVentas));
        return VentaMapper.toDTO(venta);
    }

    @Override
    public List<VentaResponseDTO> listarTodasVentas() {
        return ventaRepository.findAll().stream()
                .map(VentaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
