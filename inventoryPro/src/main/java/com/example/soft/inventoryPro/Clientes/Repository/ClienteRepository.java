package com.example.soft.inventoryPro.Clientes.Repository;

import com.example.soft.inventoryPro.Clientes.Models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findClienteByDocumento(Integer documento);
    Long countByEstado(String estado);
}
