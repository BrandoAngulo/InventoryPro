package com.example.soft.inventoryPro.Secuencia.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contador persistente por tipo de documento. Una fila por {@link TipoSecuencia}.
 * El valor se incrementa dentro de la transacción que crea el pedido/factura, bajo
 * bloqueo pesimista, para que la numeración sea secuencial, sobreviva reinicios y
 * sea segura ante concurrencia. Reemplaza al contador en memoria (AtomicLong).
 */
@Entity
@Table(name = "secuencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Secuencia {

    @Id
    @Column(length = 30)
    private String tipo;

    @Column(nullable = false)
    private Long valor;
}
