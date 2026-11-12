package br.com.oficina.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrdemServicoRequestDTO(
    @NotNull(message = "O ID do equipamento é obrigatório") Long equipamentoId,
    Long tecnicoId,
    @NotBlank(message = "A descrição do defeito relatado é obrigatória") String defeitoRelatado
) {}
