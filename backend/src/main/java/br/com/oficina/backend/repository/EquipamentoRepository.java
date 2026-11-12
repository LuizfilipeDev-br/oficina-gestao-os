package br.com.oficina.backend.repository;

import br.com.oficina.backend.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
    List<Equipamento> findByClienteId(Long clienteId);
}

