package br.com.oficina.backend.service;

import br.com.oficina.backend.dto.EquipamentoRequestDTO;
import br.com.oficina.backend.dto.EquipamentoResponseDTO;
import br.com.oficina.backend.model.Cliente;
import br.com.oficina.backend.model.Equipamento;
import br.com.oficina.backend.repository.ClienteRepository;
import br.com.oficina.backend.repository.EquipamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public EquipamentoResponseDTO cadastrar(EquipamentoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com ID: " + dto.clienteId()));

        Equipamento equipamento = Equipamento.builder()
                .cliente(cliente)
                .tipo(dto.tipo())
                .marca(dto.marca())
                .modelo(dto.modelo())
                .numeroSerie(dto.numeroSerie())
                .especificacoesHardware(dto.especificacoesHardware())
                .observacoesFisicas(dto.observacoesFisicas())
                .build();

        return EquipamentoResponseDTO.fromEntity(equipamentoRepository.save(equipamento));
    }

    @Transactional(readOnly = true)
    public List<EquipamentoResponseDTO> listarPorCliente(Long clienteId) {
        return equipamentoRepository.findByClienteId(clienteId).stream()
                .map(EquipamentoResponseDTO::fromEntity)
                .toList();
    }
}
