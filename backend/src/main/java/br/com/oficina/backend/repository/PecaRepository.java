package br.com.oficina.backend.repository;

import br.com.oficina.backend.model.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PecaRepository extends JpaRepository<Peca, Long> {
    Optional<Peca> findByCodigoSku(String codigoSku);

    @Query("SELECT p FROM Peca p WHERE p.quantidadeEstoque <= p.estoqueMinimo")
    List<Peca> findPecasComEstoqueBaixo();
}

