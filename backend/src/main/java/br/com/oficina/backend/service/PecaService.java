package br.com.oficina.backend.service;

import br.com.oficina.backend.dto.PecaRequestDTO;
import br.com.oficina.backend.dto.PecaResponseDTO;
import br.com.oficina.backend.model.Peca;
import br.com.oficina.backend.repository.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PecaService {

    private final PecaRepository pecaRepository;

    @Transactional
    public PecaResponseDTO cadastrar(PecaRequestDTO dto) {
        if (pecaRepository.findByCodigoSku(dto.codigoSku()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma peça com o SKU: " + dto.codigoSku());
        }
        Peca peca = Peca.builder()
                .codigoSku(dto.codigoSku())
                .nome(dto.nome())
                .categoria(dto.categoria())
                .quantidadeEstoque(dto.quantidadeEstoque())
                .estoqueMinimo(dto.estoqueMinimo())
                .precoCusto(dto.precoCusto())
                .precoVenda(dto.precoVenda())
                .build();
        return PecaResponseDTO.fromEntity(pecaRepository.save(peca));
    }

    @Transactional(readOnly = true)
    public List<PecaResponseDTO> listarTodas() {
        return pecaRepository.findAll().stream().map(PecaResponseDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<PecaResponseDTO> listarAlertasEstoqueBaixo() {
        return pecaRepository.findPecasComEstoqueBaixo().stream().map(PecaResponseDTO::fromEntity).toList();
    }

    @Transactional
    public PecaResponseDTO adicionarEstoque(Long id, Integer quantidadeAdicionar) {
        if (quantidadeAdicionar == null || quantidadeAdicionar <= 0) {
            throw new IllegalArgumentException("A quantidade a adicionar deve ser maior que zero.");
        }
        Peca peca = pecaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Peça não encontrada com ID: " + id));
        peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() + quantidadeAdicionar);
        return PecaResponseDTO.fromEntity(pecaRepository.save(peca));
    }
}
