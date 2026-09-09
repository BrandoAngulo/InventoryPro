package com.example.soft.inventoryPro.Pedido.Repository;

import com.example.soft.inventoryPro.Pedido.Models.EstadoPedido;
import com.example.soft.inventoryPro.Pedido.Models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByCliente_IdCliente(Integer clienteId);
    List<Pedido> findByEstadoAndCliente_IdCliente(EstadoPedido estado, Integer clienteId);
}
