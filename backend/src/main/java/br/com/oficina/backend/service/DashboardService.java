package br.com.oficina.backend.service;

import br.com.oficina.backend.dto.DashboardResponseDTO;
import br.com.oficina.backend.dto.PecaResponseDTO;
import br.com.oficina.backend.enums.StatusOrdemServico;
import br.com.oficina.backend.model.OrdemServico;
import br.com.oficina.backend.repository.ClienteRepository;
import br.com.oficina.backend.repository.OrdemServicoRepository;
import br.com.oficina.backend.repository.OsPecaUtilizadaRepository;
import br.com.oficina.backend.repository.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final OrdemServicoRepository osRepository;
    private final OsPecaUtilizadaRepository osPecaRepository;
    private final PecaRepository pecaRepository;

    @Transactional(readOnly = true)
    public DashboardResponseDTO obterMetricas() {
        long totalClientes = clienteRepository.count();
        List<OrdemServico> todasOs = osRepository.findAll();

        long osFinalizadas = todasOs.stream()
                .filter(os -> os.getStatus() == StatusOrdemServico.FINALIZADA)
                .count();

        long osEmAndamento = todasOs.stream()
                .filter(os -> os.getStatus() != StatusOrdemServico.FINALIZADA && os.getStatus() != StatusOrdemServico.CANCELADA)
                .count();

        BigDecimal faturamento = todasOs.stream()
                .filter(os -> os.getStatus() == StatusOrdemServico.FINALIZADA)
                .map(OrdemServico::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal custoPecas = osPecaRepository.calcularCustoTotalPecasFinalizadas();
        BigDecimal lucroLiquido = faturamento.subtract(custoPecas);

        List<PecaResponseDTO> alertas = pecaRepository.findPecasComEstoqueBaixo().stream()
                .map(PecaResponseDTO::fromEntity)
                .toList();

        return new DashboardResponseDTO(
                totalClientes, osEmAndamento, osFinalizadas,
                faturamento, custoPecas, lucroLiquido, alertas
        );
    }
}
