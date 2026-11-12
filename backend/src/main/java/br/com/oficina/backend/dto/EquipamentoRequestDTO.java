package br.com.oficina.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipamentoRequestDTO(
    @NotNull(message = "ID do cliente é obrigatório") Long clienteId,
    @NotBlank(message = "Tipo do equipamento é obrigatório") String tipo,
    @NotBlank(message = "Marca é obrigatória") String marca,
    @NotBlank(message = "Modelo é obrigatório") String modelo,
    String numeroSerie,
    String especificacoesHardware,
    String observacoesFisicas
) {}
