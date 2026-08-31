package com.example.soft.inventoryPro.venta.Service.Impl;

import com.example.soft.inventoryPro.Bodega.Repository.BodegaRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VentaImpl implements VentaService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductoRepository productosRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    BodegaRepository bodegaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Override
    public Ventas registrarVentas(Ventas ventas) {

        Cliente clientes = clienteRepository.findById(ventas.getClientes().getIdCliente()).orElseThrow
                (() -> new RuntimeException("Cliente no encontrado con ID: " + ventas.getClientes().getIdCliente()));

        Usuario usuarios = usuarioRepository.findById(ventas.getUsuario().getIdUsuario()).orElseThrow
                (() -> new RuntimeException("Usuario no encontrado con ID: " + ventas.getUsuario().getIdUsuario()));


        Set<Productos> productosList = ventas.getProductos().stream()
                .map(producto -> productosRepository.findById(producto.getIdProductos()).orElseThrow(
                        () -> new RuntimeException("Stock no encontrado")))
                .collect(Collectors.toSet());


        List<Stock> stockList = stockRepository.findByProductosIn(productosList);


        Map<Productos, Stock> stockMap = stockList.stream()
                .collect(Collectors.toMap(Stock::getProductos, stock -> stock));




        List<Productos> ped = ventas.getProductos().stream().collect(Collectors.toList());

        try{

            int total = 0;
            List<DetalleVenta> detallesVenta = new ArrayList<>();
            Ventas venta = new Ventas();
            venta.setFecha(LocalDate.now());

            for (Productos pedido : ped) {
                // Buscar el producto real desde productosList (por ID)
                Productos productoBase = productosList.stream()
                        .filter(p -> p.getIdProductos().equals(pedido.getIdProductos()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + pedido.getIdProductos()));

                // Buscar el stock correspondiente
                Stock stock = stockMap.get(productoBase);
                if (stock == null) {
                    throw new RuntimeException("Stock no encontrado para el producto: " + productoBase.getNombreProducto());
                }

                // Validar stock
                if (stock.getCantidadStock() < pedido.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + productoBase.getNombreProducto());
                }

                // Descontar stock
                int stockActualizado = stock.getCantidadStock() - pedido.getCantidad();
                stock.setCantidadStock(stockActualizado);
                stockRepository.save(stock);

                // Calcular total
                int subtotal = productoBase.getPrecioVenta() * pedido.getCantidad();
                total += subtotal;

                // Crear detalle
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVentas(venta);
                detalle.setProductos(productoBase);
                detalle.setCantidad(pedido.getCantidad());
                detalle.setPrecioUnitario(productoBase.getPrecioVenta());
                detallesVenta.add(detalle);
            }

            venta.setTotal(total);
            venta.setDetalles(detallesVenta);
            venta.setClientes(clientes);
            venta.setUsuario(usuarios);

            Ventas ventaRegistrada = ventaRepository.save(venta);
            return ventaRegistrada;
        }catch (Exception e){
            throw new RuntimeException(e);
        }}


    @Override
    public Ventas listarVentas(Integer idVentas) {
            Ventas ventas = ventaRepository.findById(idVentas)
                    .orElseThrow(() -> new RuntimeException("venta no encontrado"));

            try{



                return ventas;
            }catch (Exception e){
                throw new RuntimeException(e);
            }
    }

    @Override
    public List<Ventas> listarTodasVentas() {
        try{
            List<Ventas> ventas = ventaRepository.findAll();
            if (ventas.isEmpty()) {

                throw new RuntimeException("La lista esta vacia");
            }
            return ventas;
        }catch  (Exception e){
            throw new RuntimeException(e);

        }
    }
}
