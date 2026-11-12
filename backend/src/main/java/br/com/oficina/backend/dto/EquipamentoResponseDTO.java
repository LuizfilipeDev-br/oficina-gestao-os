package br.com.oficina.backend.dto;

import br.com.oficina.backend.model.Equipamento;

public record EquipamentoResponseDTO(
    Long id, Long clienteId, String clienteNome, String tipo, String marca, String modelo,
    String numeroSerie, String especificacoesHardware, String observacoesFisicas
) {
    public static EquipamentoResponseDTO fromEntity(Equipamento e) {
        return new EquipamentoResponseDTO(
            e.getId(), e.getCliente().getId(), e.getCliente().getNome(), e.getTipo(),
            e.getMarca(), e.getModelo(), e.getNumeroSerie(), e.getEspecificacoesHardware(), e.getObservacoesFisicas()
        );
    }
}
