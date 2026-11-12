package br.com.oficina.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AdicionarPecaOsDTO(
    @NotNull(message = "O ID da peça é obrigatório") Long pecaId,
    @NotNull @Min(value = 1, message = "A quantidade deve ser no mínimo 1") Integer quantidade
) {}
