package com.example.soft.inventoryPro.Dashboard.Service;

import com.example.soft.inventoryPro.Dashboard.dto.DashboardResumenDTO;

public interface DashboardService {

    /**
     * Calcula y retorna el resumen consolidado de KPIs operativos para el Dashboard.
     *
     * @return DTO con métricas de ventas, saldo de caja, conteo de stock bajo, clientes y actividad reciente.
     */
    DashboardResumenDTO obtenerResumen();
}
