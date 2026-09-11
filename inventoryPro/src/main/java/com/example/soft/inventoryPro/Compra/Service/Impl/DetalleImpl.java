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
import com.example.soft.inventoryPro.common.exception.BusinessRuleException;
import com.example.soft.inventoryPro.common.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proveedor no encontrado con ID: " + compra.getProovedor().getIdProovedor()
                ));

        // 2. Asociar proveedor real
        compra.setProovedor(proveedor);

        Long total = 0L;

        // 3. Recorrer los productos de la compra
        for (DetalleCompra detalle : compra.getDetalles()) {

            // 4. Buscar producto
            Productos producto = productoRepository
                    .findById(detalle.getProducto().getIdProductos())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado con ID: " + detalle.getProducto().getIdProductos()
                    ));

            Stock stock = stockRepository
                    .findByProductos_IdProductos(
                            producto.getIdProductos()
                    )
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Stock no encontrado para el producto: " + producto.getNombreProducto()
                    ));

            // 5. Validar cantidad
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new BusinessRuleException("La cantidad debe ser mayor que 0");
            }

            // 6. Validar precio
            if (detalle.getPrecioCompra() == null || detalle.getPrecioCompra() <= 0L) {
                throw new BusinessRuleException("El precio de compra debe ser mayor que 0");
            }

            // 7. Calcular subtotal
            Long subtotal = detalle.getPrecioCompra() * detalle.getCantidad();

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
            total += subtotal;
        }

        // 13. Establecer total de la compra
        compra.setTotal(total);

        // 14. Guardar compra y sus detalles
        return compraRepository.save(compra);
    }

    @Override
    @Transactional
    public Compra actualizarCompra(Compra compra) {
        return null;
    }

    @Override
    public String eliminarProovedor(Long id) {
        return "";
    }

    @Override
    @Transactional(readOnly = true)
    public Compra findByIdCompra(Long id) {
        return compraRepository.findByIdWithDetails(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Compra> findByAllCompra() {
        return compraRepository.findAllWithDetails();
    }
}
