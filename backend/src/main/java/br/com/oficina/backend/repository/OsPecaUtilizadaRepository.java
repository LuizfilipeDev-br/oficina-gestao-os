package br.com.oficina.backend.repository;

import br.com.oficina.backend.model.OsPecaUtilizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.util.List;

public interface OsPecaUtilizadaRepository extends JpaRepository<OsPecaUtilizada, Long> {
    List<OsPecaUtilizada> findByOrdemServicoId(Long ordemServicoId);

    @Query("SELECT COALESCE(SUM(op.custoUnitarioEpoca * op.quantidade), 0) FROM OsPecaUtilizada op WHERE op.ordemServico.status = 'FINALIZADA'")
    BigDecimal calcularCustoTotalPecasFinalizadas();
}
