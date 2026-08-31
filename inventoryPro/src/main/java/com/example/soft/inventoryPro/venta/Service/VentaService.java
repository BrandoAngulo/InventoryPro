package com.example.soft.inventoryPro.venta.Service;

import com.example.soft.inventoryPro.venta.Models.Ventas;

import java.util.List;

public interface VentaService {
    Ventas registrarVentas(Ventas ventas);

    Ventas listarVentas(Integer idVentas);

    List<Ventas> listarTodasVentas();
}
