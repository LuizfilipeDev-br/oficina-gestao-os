package br.com.oficina.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "E-mail é obrigatório") @Email String email,
    @NotBlank(message = "Senha é obrigatória") String senha
) {}
