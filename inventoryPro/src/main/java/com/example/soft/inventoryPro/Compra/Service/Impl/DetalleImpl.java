package com.example.soft.inventoryPro.Compra.Service.Impl;

import com.example.soft.inventoryPro.Compra.Models.Compra;
import com.example.soft.inventoryPro.Compra.Models.DetalleCompra;
import com.example.soft.inventoryPro.Compra.Repository.CompraRepository;
import com.example.soft.inventoryPro.Compra.Repository.DetalleCompraRepository;
import com.example.soft.inventoryPro.Compra.Service.DetalleService;
import com.example.soft.inventoryPro.Productos.Models.Productos;
import com.example.soft.inventoryPro.Productos.Repository.ProductoRepository;
import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import com.example.soft.inventoryPro.Proovedor.Repository.ProovedorRepository;
import com.example.soft.inventoryPro.Stock.Models.Stock;
import com.example.soft.inventoryPro.Stock.Repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
public class DetalleImpl implements DetalleService {

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProovedorRepository proveedorRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private StockRepository stockRepository;

    @Override
    @Transactional
    public Compra registrarCompra(Compra compra) {
        // 1. Buscar proveedor
        Proovedor proveedor = proveedorRepository
                .findById(compra.getProovedor().getIdProovedor())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Proveedor no encontrado con ID: "
                                + compra.getProovedor().getIdProovedor()
                ));

        // 2. Asociar proveedor real
        compra.setProovedor(proveedor);

        BigDecimal total = BigDecimal.ZERO;

        // 3. Recorrer los productos de la compra
        for (DetalleCompra detalle : compra.getDetalles()) {

            // 4. Buscar producto
            Productos producto = productoRepository
                    .findById(detalle.getProducto().getIdProductos())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Producto no encontrado con ID: "
                                    + detalle.getProducto().getIdProductos()
                    ));

            Stock stock = stockRepository
                    .findByProductos_IdProductos(
                            producto.getIdProductos()
                    )
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Stock no encontrado para el producto: "
                                    + producto.getNombreProducto()
                    ));

            // 5. Validar cantidad
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La cantidad debe ser mayor que 0"
                );
            }

            // 6. Validar precio
            if (detalle.getPrecioCompra() == null || detalle.getPrecioCompra().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El precio de compra debe ser mayor que 0"
                );
            }

            // 7. Calcular subtotal
            BigDecimal subtotal = detalle.getPrecioCompra()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);

            detalle.setSubtotal(subtotal);

            // 8. Asociar producto real
            detalle.setProducto(producto);

            // 9. Asociar detalle con la compra
            detalle.setCompra(compra);

            stock.setCantidadStock(
                    stock.getCantidadStock() + detalle.getCantidad()
            );

            stockRepository.save(stock);

            // 12. Acumular total
            total = total.add(subtotal);
        }

        // 13. Establecer total de la compra
        compra.setTotal(total);

        // 14. Guardar compra y sus detalles
        return compraRepository.save(compra);
    }

    @Override
    public Compra actualizarCompra(Compra compra) {
        return null;
    }

    @Override
    public String eliminarProovedor(Integer id) {
        return "";
    }

    @Override
    public Compra findByIdCompra(Integer id) {
        return null;
    }

    @Override
    public List<Compra> findByAllCompra() {
        return List.of();
    }
}
