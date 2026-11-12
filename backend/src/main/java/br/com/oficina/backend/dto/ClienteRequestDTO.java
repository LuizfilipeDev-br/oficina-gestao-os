package br.com.oficina.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClienteRequestDTO(
    @NotBlank(message = "O nome é obrigatório") String nome,
    @NotBlank(message = "O CPF é obrigatório") @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos") String cpf,
    @NotBlank(message = "O telefone é obrigatório") String telefone,
    @Email(message = "E-mail inválido") String email
) {}

