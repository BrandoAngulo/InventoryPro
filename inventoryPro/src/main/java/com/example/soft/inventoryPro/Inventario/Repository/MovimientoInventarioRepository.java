package com.example.soft.inventoryPro.Inventario.Repository;

import com.example.soft.inventoryPro.Inventario.Models.MovimientoInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    List<MovimientoInventario> findByProducto_IdProductos(Integer productoId);
    List<MovimientoInventario> findByTipoMovimiento(TipoMovimiento tipoMovimiento);
    List<MovimientoInventario> findByProducto_IdProductosAndTipoMovimiento(Integer productoId, TipoMovimiento tipoMovimiento);
}
