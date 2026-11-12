package br.com.oficina.backend.controller;

import br.com.oficina.backend.dto.DashboardResponseDTO;
import br.com.oficina.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> obterMetricas() {
        return ResponseEntity.ok(dashboardService.obterMetricas());
    }
}
