package com.example.soft.inventoryPro.Secuencia.Models;

/**
 * Tipos de numeración persistente del sistema. El prefijo es el que ve el usuario
 * final en el documento (PED-000123 / FAC-000123) y es parte del contrato con el frontend.
 */
public enum TipoSecuencia {

    PEDIDO("PED"),
    FACTURA("FAC");

    private final String prefijo;

    TipoSecuencia(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getPrefijo() {
        return prefijo;
    }
}
