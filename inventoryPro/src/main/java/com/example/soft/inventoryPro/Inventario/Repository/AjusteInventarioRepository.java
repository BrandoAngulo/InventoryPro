package com.example.soft.inventoryPro.Inventario.Repository;

import com.example.soft.inventoryPro.Inventario.Models.AjusteInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoAjuste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AjusteInventarioRepository extends JpaRepository<AjusteInventario, Long> {
    @Query("SELECT DISTINCT a FROM AjusteInventario a JOIN FETCH a.producto WHERE a.producto.idProductos = :productoId")
    List<AjusteInventario> findByProducto_IdProductos(@Param("productoId") Long productoId);

    @Query("SELECT DISTINCT a FROM AjusteInventario a JOIN FETCH a.producto WHERE a.tipoAjuste = :tipoAjuste")
    List<AjusteInventario> findByTipoAjuste(@Param("tipoAjuste") TipoAjuste tipoAjuste);

    @Query("SELECT DISTINCT a FROM AjusteInventario a JOIN FETCH a.producto WHERE a.producto.idProductos = :productoId AND a.tipoAjuste = :tipoAjuste")
    List<AjusteInventario> findByProducto_IdProductosAndTipoAjuste(@Param("productoId") Long productoId, @Param("tipoAjuste") TipoAjuste tipoAjuste);

    @Query("SELECT DISTINCT a FROM AjusteInventario a JOIN FETCH a.producto ORDER BY a.fecha DESC")
    List<AjusteInventario> findAllByOrderByFechaDesc();
}
