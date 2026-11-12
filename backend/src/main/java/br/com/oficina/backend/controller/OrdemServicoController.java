package br.com.oficina.backend.controller;

import br.com.oficina.backend.dto.*;
import br.com.oficina.backend.service.OrdemServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService osService;

    @PostMapping
    public ResponseEntity<OrdemServicoResponseDTO> abrirOs(@RequestBody @Valid OrdemServicoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(osService.abrirOs(dto));
    }

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(osService.listarTodas());
    }

    @PostMapping("/{id}/pecas")
    public ResponseEntity<OrdemServicoResponseDTO> adicionarPeca(@PathVariable Long id, @RequestBody @Valid AdicionarPecaOsDTO dto) {
        return ResponseEntity.ok(osService.adicionarPeca(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrdemServicoResponseDTO> atualizarStatus(@PathVariable Long id, @RequestBody @Valid AtualizarStatusOsDTO dto) {
        return ResponseEntity.ok(osService.atualizarStatus(id, dto));
    }

    // --- ROTAS PÚBLICAS DO CLIENTE ---

    @GetMapping("/publico/consulta")
    public ResponseEntity<OrdemServicoResponseDTO> consultarPublico(@RequestParam String protocolo, @RequestParam String cpf) {
        return ResponseEntity.ok(osService.consultarPublico(protocolo, cpf));
    }

    @PostMapping("/publico/aprovar")
    public ResponseEntity<OrdemServicoResponseDTO> aprovarPublico(@RequestParam String protocolo, @RequestParam String cpf) {
        return ResponseEntity.ok(osService.aprovarOrcamentoPublico(protocolo, cpf));
    }
}
