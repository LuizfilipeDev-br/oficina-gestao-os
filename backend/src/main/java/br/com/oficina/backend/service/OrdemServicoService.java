package br.com.oficina.backend.service;

import br.com.oficina.backend.dto.*;
import br.com.oficina.backend.enums.StatusOrdemServico;
import br.com.oficina.backend.model.*;
import br.com.oficina.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository osRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PecaRepository pecaRepository;
    private final OsPecaUtilizadaRepository osPecaRepository;
    private final OsHistoricoStatusRepository historicoRepository;

    @Transactional
    public OrdemServicoResponseDTO abrirOs(OrdemServicoRequestDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(dto.equipamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado com ID: " + dto.equipamentoId()));

        Usuario tecnico = null;
        if (dto.tecnicoId() != null) {
            tecnico = usuarioRepository.findById(dto.tecnicoId())
                    .orElseThrow(() -> new IllegalArgumentException("Técnico não encontrado com ID: " + dto.tecnicoId()));
        }

        String protocolo = "OS-" + Year.now().getValue() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        OrdemServico os = OrdemServico.builder()
                .protocolo(protocolo)
                .equipamento(equipamento)
                .tecnico(tecnico)
                .status(StatusOrdemServico.ABERTA)
                .defeitoRelatado(dto.defeitoRelatado())
                .build();

        os = osRepository.save(os);
        registrarHistorico(os, tecnico, null, StatusOrdemServico.ABERTA, "Ordem de Serviço aberta no sistema.");
        return montarResponse(os);
    }

    @Transactional
    public OrdemServicoResponseDTO adicionarPeca(Long osId, AdicionarPecaOsDTO dto) {
        OrdemServico os = buscarEntidadeOs(osId);
        if (os.getStatus() == StatusOrdemServico.FINALIZADA || os.getStatus() == StatusOrdemServico.CANCELADA) {
            throw new IllegalArgumentException("Não é possível adicionar peças em uma OS finalizada ou cancelada.");
        }

        Peca peca = pecaRepository.findById(dto.pecaId())
                .orElseThrow(() -> new IllegalArgumentException("Peça não encontrada com ID: " + dto.pecaId()));

        if (peca.getQuantidadeEstoque() < dto.quantidade()) {
            throw new IllegalArgumentException("Estoque insuficiente para a peça '" + peca.getNome() + "'. Disponível: " + peca.getQuantidadeEstoque());
        }

        // Baixa automática no estoque
        peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() - dto.quantidade());
        pecaRepository.save(peca);

        // Registra a peça congelando preço e custo da época
        OsPecaUtilizada item = OsPecaUtilizada.builder()
                .ordemServico(os)
                .peca(peca)
                .quantidade(dto.quantidade())
                .precoUnitarioCobrado(peca.getPrecoVenda())
                .custoUnitarioEpoca(peca.getPrecoCusto())
                .build();
        osPecaRepository.save(item);

        // Recalcula o total das peças e o valor total da OS
        BigDecimal subtotalItem = peca.getPrecoVenda().multiply(BigDecimal.valueOf(dto.quantidade()));
        os.setValorTotalPecas(os.getValorTotalPecas().add(subtotalItem));
        os.setValorTotal(os.getValorMaoDeObra().add(os.getValorTotalPecas()));

        return montarResponse(osRepository.save(os));
    }

    @Transactional
    public OrdemServicoResponseDTO atualizarStatus(Long osId, AtualizarStatusOsDTO dto) {
        OrdemServico os = buscarEntidadeOs(osId);
        StatusOrdemServico statusAnterior = os.getStatus();

        Usuario tecnico = null;
        if (dto.tecnicoId() != null) {
            tecnico = usuarioRepository.findById(dto.tecnicoId())
                    .orElseThrow(() -> new IllegalArgumentException("Técnico não encontrado com ID: " + dto.tecnicoId()));
            os.setTecnico(tecnico);
        }

        if (dto.diagnosticoTecnico() != null) {
            os.setDiagnosticoTecnico(dto.diagnosticoTecnico());
        }

        if (dto.valorMaoDeObra() != null) {
            os.setValorMaoDeObra(dto.valorMaoDeObra());
            os.setValorTotal(os.getValorMaoDeObra().add(os.getValorTotalPecas()));
        }

        if (dto.novoStatus() == StatusOrdemServico.FINALIZADA) {
            os.setDataFinalizacao(LocalDateTime.now());
        }

        os.setStatus(dto.novoStatus());
        os = osRepository.save(os);

        String obs = (dto.observacaoHistorico() != null && !dto.observacaoHistorico().isBlank())
                ? dto.observacaoHistorico()
                : "Status alterado para " + dto.novoStatus();

        registrarHistorico(os, tecnico, statusAnterior, dto.novoStatus(), obs);
        return montarResponse(os);
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoResponseDTO> listarTodas() {
        return osRepository.findAll().stream().map(this::montarResponse).toList();
    }

    // --- PORTAL PÚBLICO DO CLIENTE ---

    @Transactional(readOnly = true)
    public OrdemServicoResponseDTO consultarPublico(String protocolo, String cpf) {
        OrdemServico os = osRepository.findByProtocoloAndEquipamentoClienteCpf(protocolo, cpf)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada para o protocolo e CPF informados."));
        return montarResponse(os);
    }

    @Transactional
    public OrdemServicoResponseDTO aprovarOrcamentoPublico(String protocolo, String cpf) {
        OrdemServico os = osRepository.findByProtocoloAndEquipamentoClienteCpf(protocolo, cpf)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada para o protocolo e CPF informados."));

        if (os.getStatus() != StatusOrdemServico.AGUARDANDO_APROVACAO) {
            throw new IllegalArgumentException("Esta OS não está aguardando aprovação no momento (Status atual: " + os.getStatus() + ").");
        }

        StatusOrdemServico anterior = os.getStatus();
        os.setOrcamentoAprovado(true);
        os.setDataAprovacao(LocalDateTime.now());
        os.setStatus(StatusOrdemServico.APROVADA);
        os = osRepository.save(os);

        registrarHistorico(os, null, anterior, StatusOrdemServico.APROVADA, "Orçamento aprovado pelo cliente via Portal Público.");
        return montarResponse(os);
    }

    private OrdemServico buscarEntidadeOs(Long id) {
        return osRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada com ID: " + id));
    }

    private void registrarHistorico(OrdemServico os, Usuario usuario, StatusOrdemServico anterior, StatusOrdemServico novo, String obs) {
        OsHistoricoStatus historico = OsHistoricoStatus.builder()
                .ordemServico(os)
                .usuario(usuario)
                .statusAnterior(anterior)
                .statusNovo(novo)
                .observacao(obs)
                .build();
        historicoRepository.save(historico);
    }

    private OrdemServicoResponseDTO montarResponse(OrdemServico os) {
        List<OsHistoricoResponseDTO> historico = historicoRepository
                .findByOrdemServicoIdOrderByDataAlteracaoAsc(os.getId())
                .stream()
                .map(OsHistoricoResponseDTO::fromEntity)
                .toList();
        return OrdemServicoResponseDTO.fromEntity(os, historico);
    }
}
