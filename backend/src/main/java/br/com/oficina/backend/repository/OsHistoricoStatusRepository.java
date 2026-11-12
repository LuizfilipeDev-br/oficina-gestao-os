package br.com.oficina.backend.repository;

import br.com.oficina.backend.model.OsHistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OsHistoricoStatusRepository extends JpaRepository<OsHistoricoStatus, Long> {
    List<OsHistoricoStatus> findByOrdemServicoIdOrderByDataAlteracaoAsc(Long ordemServicoId);
}
