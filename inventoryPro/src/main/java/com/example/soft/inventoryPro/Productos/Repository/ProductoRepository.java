package com.example.soft.inventoryPro.Productos.Repository;

import com.example.soft.inventoryPro.Productos.Models.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Productos, Long> {
    Optional<Productos> findByIdProductos(Long idProductos);

    boolean existsByCodigoIgnoreCase(String codigo);

    boolean existsByCodigoIgnoreCaseAndIdProductosNot(String codigo, Long idProductos);

    @Query("SELECT p FROM Productos p WHERE p.cantidad <= p.stockMinimo AND (p.activo = true OR p.activo IS NULL)")
    List<Productos> findProductosBajoStock();

    @Query("SELECT DISTINCT p FROM Productos p LEFT JOIN FETCH p.categorias LEFT JOIN FETCH p.Bodega LEFT JOIN FETCH p.proveedores WHERE p.idProductos = :id")
    Optional<Productos> findByIdWithAssociations(@Param("id") Long id);
}
