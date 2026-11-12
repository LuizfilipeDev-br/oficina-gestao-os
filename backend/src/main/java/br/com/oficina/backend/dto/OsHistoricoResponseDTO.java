package br.com.oficina.backend.dto;

import br.com.oficina.backend.enums.StatusOrdemServico;
import br.com.oficina.backend.model.OsHistoricoStatus;
import java.time.LocalDateTime;

public record OsHistoricoResponseDTO(
    Long id,
    StatusOrdemServico statusAnterior,
    StatusOrdemServico statusNovo,
    String observacao,
    String responsavel,
    LocalDateTime dataAlteracao
) {
    public static OsHistoricoResponseDTO fromEntity(OsHistoricoStatus h) {
        String nomeResp = (h.getUsuario() != null) ? h.getUsuario().getNome() : "Cliente (Portal Público)";
        return new OsHistoricoResponseDTO(
            h.getId(), h.getStatusAnterior(), h.getStatusNovo(), h.getObservacao(), nomeResp, h.getDataAlteracao()
        );
    }
}
