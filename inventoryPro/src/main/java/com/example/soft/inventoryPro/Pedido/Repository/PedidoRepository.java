package com.example.soft.inventoryPro.Pedido.Repository;

import com.example.soft.inventoryPro.Pedido.Models.EstadoPedido;
import com.example.soft.inventoryPro.Pedido.Models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstado(EstadoPedido estado);

    @Query("SELECT p.numeroPedido FROM Pedido p")
    List<String> findAllNumerosPedido();
    List<Pedido> findByCliente_IdCliente(Long clienteId);
    List<Pedido> findByEstadoAndCliente_IdCliente(EstadoPedido estado, Long clienteId);

    @Query("SELECT DISTINCT p FROM Pedido p JOIN FETCH p.cliente LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.producto WHERE p.id = :id")
    Optional<Pedido> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Pedido p JOIN FETCH p.cliente LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.producto ORDER BY p.fechaCreacion DESC")
    List<Pedido> findAllWithDetails();

    @Query("SELECT DISTINCT p FROM Pedido p JOIN FETCH p.cliente LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.producto WHERE p.estado = :estado ORDER BY p.fechaCreacion DESC")
    List<Pedido> findByEstadoWithDetails(@Param("estado") EstadoPedido estado);

    @Query("SELECT DISTINCT p FROM Pedido p JOIN FETCH p.cliente LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.producto WHERE p.cliente.idCliente = :clienteId ORDER BY p.fechaCreacion DESC")
    List<Pedido> findByClienteIdWithDetails(@Param("clienteId") Long clienteId);

    @Query("SELECT DISTINCT p FROM Pedido p JOIN FETCH p.cliente LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.producto WHERE p.estado = :estado AND p.cliente.idCliente = :clienteId ORDER BY p.fechaCreacion DESC")
    List<Pedido> findByEstadoAndClienteIdWithDetails(@Param("estado") EstadoPedido estado, @Param("clienteId") Long clienteId);
}
