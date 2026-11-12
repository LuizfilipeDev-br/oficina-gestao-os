package br.com.oficina.backend.dto;

import br.com.oficina.backend.enums.StatusOrdemServico;
import br.com.oficina.backend.model.OrdemServico;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdemServicoResponseDTO(
    Long id,
    String protocolo,
    StatusOrdemServico status,
    String clienteNome,
    String equipamentoDescricao,
    String tecnicoNome,
    String defeitoRelatado,
    String diagnosticoTecnico,
    BigDecimal valorMaoDeObra,
    BigDecimal valorTotalPecas,
    BigDecimal valorTotal,
    Boolean orcamentoAprovado,
    LocalDateTime dataAbertura,
    LocalDateTime dataAprovacao,
    LocalDateTime dataFinalizacao,
    List<OsHistoricoResponseDTO> historico
) {
    public static OrdemServicoResponseDTO fromEntity(OrdemServico os, List<OsHistoricoResponseDTO> historico) {
        String equip = os.getEquipamento().getTipo() + " " + os.getEquipamento().getMarca() + " " + os.getEquipamento().getModelo();
        String tec = (os.getTecnico() != null) ? os.getTecnico().getNome() : "Não atribuído";
        return new OrdemServicoResponseDTO(
            os.getId(), os.getProtocolo(), os.getStatus(), os.getEquipamento().getCliente().getNome(),
            equip, tec, os.getDefeitoRelatado(), os.getDiagnosticoTecnico(),
            os.getValorMaoDeObra(), os.getValorTotalPecas(), os.getValorTotal(),
            os.getOrcamentoAprovado(), os.getDataAbertura(), os.getDataAprovacao(), os.getDataFinalizacao(),
            historico
        );
    }
}
