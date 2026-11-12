package br.com.oficina.backend.dto;

import br.com.oficina.backend.enums.StatusOrdemServico;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AtualizarStatusOsDTO(
    @NotNull(message = "O novo status é obrigatório") StatusOrdemServico novoStatus,
    Long tecnicoId,
    String diagnosticoTecnico,
    BigDecimal valorMaoDeObra,
    String observacaoHistorico
) {}
