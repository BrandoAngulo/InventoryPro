package com.example.soft.inventoryPro.Dashboard.Controller;

import com.example.soft.inventoryPro.Dashboard.Service.DashboardService;
import com.example.soft.inventoryPro.Dashboard.dto.DashboardResumenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/resumen")
    public ResponseEntity<DashboardResumenDTO> obtenerResumen() {
        DashboardResumenDTO resumen = dashboardService.obtenerResumen();
        return ResponseEntity.ok(resumen);
    }
}
