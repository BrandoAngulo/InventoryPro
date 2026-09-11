package com.example.soft.inventoryPro.Proovedor.Repository;

import com.example.soft.inventoryPro.Proovedor.Models.Proovedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProovedorRepository extends JpaRepository<Proovedor, Long> {
    boolean existsByDocumento(String documento);
    boolean existsByDocumentoAndIdProovedorNot(String documento, Long idProovedor);
}
