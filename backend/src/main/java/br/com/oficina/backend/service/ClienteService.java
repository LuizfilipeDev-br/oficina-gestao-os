package br.com.oficina.backend.service;

import br.com.oficina.backend.dto.ClienteRequestDTO;
import br.com.oficina.backend.dto.ClienteResponseDTO;
import br.com.oficina.backend.model.Cliente;
import br.com.oficina.backend.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {
        if (clienteRepository.existsByCpf(dto.cpf())) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com o CPF: " + dto.cpf());
        }
        Cliente cliente = Cliente.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .telefone(dto.telefone())
                .email(dto.email())
                .build();
        return ClienteResponseDTO.fromEntity(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream().map(ClienteResponseDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com ID: " + id));
        return ClienteResponseDTO.fromEntity(cliente);
    }
}
