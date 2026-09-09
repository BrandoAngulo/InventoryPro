package com.example.soft.inventoryPro.Ultils.Errores;

import lombok.Getter;

@Getter
public class StockInsuficienteException extends RuntimeException {
    private final Long productoId;
    private final Integer stockDisponible;
    private final Integer cantidadSolicitada;

    public StockInsuficienteException(String productoNombre, Long productoId, Integer stockDisponible, Integer cantidadSolicitada) {
        super(String.format("Stock insuficiente para el producto: %s. Disponible: %d, Solicitado: %d",
                productoNombre, stockDisponible, cantidadSolicitada));
        this.productoId = productoId;
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }
}
