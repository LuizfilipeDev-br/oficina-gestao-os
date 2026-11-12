package br.com.oficina.backend.controller;

import br.com.oficina.backend.dto.EquipamentoRequestDTO;
import br.com.oficina.backend.dto.EquipamentoResponseDTO;
import br.com.oficina.backend.service.EquipamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    @PostMapping
    public ResponseEntity<EquipamentoResponseDTO> cadastrar(@RequestBody @Valid EquipamentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamentoService.cadastrar(dto));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<EquipamentoResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(equipamentoService.listarPorCliente(clienteId));
    }
}

