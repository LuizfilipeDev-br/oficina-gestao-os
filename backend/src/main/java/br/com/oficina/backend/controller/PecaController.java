package br.com.oficina.backend.controller;

import br.com.oficina.backend.dto.PecaRequestDTO;
import br.com.oficina.backend.dto.PecaResponseDTO;
import br.com.oficina.backend.service.PecaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pecas")
@RequiredArgsConstructor
public class PecaController {

    private final PecaService pecaService;

    @PostMapping
    public ResponseEntity<PecaResponseDTO> cadastrar(@RequestBody @Valid PecaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pecaService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<PecaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(pecaService.listarTodas());
    }

    @GetMapping("/alertas-estoque")
    public ResponseEntity<List<PecaResponseDTO>> listarAlertas() {
        return ResponseEntity.ok(pecaService.listarAlertasEstoqueBaixo());
    }

    @PatchMapping("/{id}/entrada-estoque")
    public ResponseEntity<PecaResponseDTO> adicionarEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        return ResponseEntity.ok(pecaService.adicionarEstoque(id, quantidade));
    }
}
