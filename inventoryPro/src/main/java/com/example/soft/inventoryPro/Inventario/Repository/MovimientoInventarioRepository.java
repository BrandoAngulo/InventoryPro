package com.example.soft.inventoryPro.Inventario.Repository;

import com.example.soft.inventoryPro.Inventario.Models.MovimientoInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    @Query("SELECT DISTINCT m FROM MovimientoInventario m JOIN FETCH m.producto WHERE m.producto.idProductos = :productoId")
    List<MovimientoInventario> findByProducto_IdProductos(@Param("productoId") Long productoId);

    @Query("SELECT DISTINCT m FROM MovimientoInventario m JOIN FETCH m.producto WHERE m.tipoMovimiento = :tipoMovimiento")
    List<MovimientoInventario> findByTipoMovimiento(@Param("tipoMovimiento") TipoMovimiento tipoMovimiento);

    @Query("SELECT DISTINCT m FROM MovimientoInventario m JOIN FETCH m.producto WHERE m.producto.idProductos = :productoId AND m.tipoMovimiento = :tipoMovimiento")
    List<MovimientoInventario> findByProducto_IdProductosAndTipoMovimiento(@Param("productoId") Long productoId, @Param("tipoMovimiento") TipoMovimiento tipoMovimiento);

    @Query("SELECT DISTINCT m FROM MovimientoInventario m JOIN FETCH m.producto ORDER BY m.fecha DESC")
    List<MovimientoInventario> findAllWithProductoOrderByFechaDesc();
}
