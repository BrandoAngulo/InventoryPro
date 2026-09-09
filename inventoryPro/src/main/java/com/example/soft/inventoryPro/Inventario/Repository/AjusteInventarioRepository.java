package com.example.soft.inventoryPro.Inventario.Repository;

import com.example.soft.inventoryPro.Inventario.Models.AjusteInventario;
import com.example.soft.inventoryPro.Inventario.Models.TipoAjuste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AjusteInventarioRepository extends JpaRepository<AjusteInventario, Long> {
    List<AjusteInventario> findByProducto_IdProductos(Integer productoId);
    List<AjusteInventario> findByTipoAjuste(TipoAjuste tipoAjuste);
    List<AjusteInventario> findByProducto_IdProductosAndTipoAjuste(Integer productoId, TipoAjuste tipoAjuste);
    List<AjusteInventario> findAllByOrderByFechaDesc();
}
