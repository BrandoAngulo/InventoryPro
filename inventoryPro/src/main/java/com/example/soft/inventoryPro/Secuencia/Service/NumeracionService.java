package com.example.soft.inventoryPro.Secuencia.Service;

import com.example.soft.inventoryPro.Secuencia.Models.TipoSecuencia;

public interface NumeracionService {

    /**
     * Devuelve el siguiente número formateado para el tipo dado (ej. "FAC-000124"),
     * incrementando el contador persistido. Pensado para invocarse dentro de la
     * transacción que crea el documento, de modo que el número quede reservado de
     * forma atómica con la inserción.
     */
    String siguiente(TipoSecuencia tipo);
}
