package com.example.soft.inventoryPro.Bodega.Service;

import com.example.soft.inventoryPro.Bodega.Models.Bodega;

import java.util.List;

public interface BodegaService {
    Bodega registrarBodega(Bodega bodega);

    List<Bodega> traerTodasBoderga();
    Bodega actualizarBodega(Bodega bodega);
    Bodega traerIdBoderga(Integer id);
}
