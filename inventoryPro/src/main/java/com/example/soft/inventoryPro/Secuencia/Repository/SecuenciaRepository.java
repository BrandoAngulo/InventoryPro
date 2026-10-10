package com.example.soft.inventoryPro.Secuencia.Repository;

import com.example.soft.inventoryPro.Secuencia.Models.Secuencia;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SecuenciaRepository extends JpaRepository<Secuencia, String> {

    /**
     * Carga la fila del contador con bloqueo de escritura (SELECT ... FOR UPDATE),
     * serializando a quien intente numerar el mismo tipo de documento en paralelo.
     * Debe ejecutarse dentro de una transacción.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Secuencia s where s.tipo = :tipo")
    Optional<Secuencia> findByTipoForUpdate(@Param("tipo") String tipo);
}
