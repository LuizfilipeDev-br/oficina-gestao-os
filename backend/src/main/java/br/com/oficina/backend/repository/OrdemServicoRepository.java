package br.com.oficina.backend.repository;

import br.com.oficina.backend.enums.StatusOrdemServico;
import br.com.oficina.backend.model.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    Optional<OrdemServico> findByProtocoloAndEquipamentoClienteCpf(String protocolo, String cpf);
    List<OrdemServico> findByStatus(StatusOrdemServico status);
}

