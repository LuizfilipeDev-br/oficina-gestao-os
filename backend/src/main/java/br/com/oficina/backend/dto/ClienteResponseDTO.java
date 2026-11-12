package br.com.oficina.backend.dto;

import br.com.oficina.backend.model.Cliente;
import java.time.LocalDateTime;

public record ClienteResponseDTO(Long id, String nome, String cpf, String telefone, String email, LocalDateTime criadoEm) {
    public static ClienteResponseDTO fromEntity(Cliente c) {
        return new ClienteResponseDTO(c.getId(), c.getNome(), c.getCpf(), c.getTelefone(), c.getEmail(), c.getCriadoEm());
    }
}
