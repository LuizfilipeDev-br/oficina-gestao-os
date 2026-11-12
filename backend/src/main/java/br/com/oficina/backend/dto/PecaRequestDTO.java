package br.com.oficina.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record PecaRequestDTO(
    @NotBlank(message = "Código SKU é obrigatório") String codigoSku,
    @NotBlank(message = "Nome da peça é obrigatório") String nome,
    String categoria,
    @NotNull @Min(value = 0, message = "Estoque não pode ser negativo") Integer quantidadeEstoque,
    @NotNull @Min(value = 1, message = "Estoque mínimo deve ser ao menos 1") Integer estoqueMinimo,
    @NotNull @Positive(message = "Preço de custo deve ser positivo") BigDecimal precoCusto,
    @NotNull @Positive(message = "Preço de venda deve ser positivo") BigDecimal precoVenda
) {}

