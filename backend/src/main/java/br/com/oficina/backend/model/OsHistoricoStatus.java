package br.com.oficina.backend.model;

import br.com.oficina.backend.enums.StatusOrdemServico;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "os_historico_status")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OsHistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", length = 30)
    private StatusOrdemServico statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false, length = 30)
    private StatusOrdemServico statusNovo;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "data_alteracao", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataAlteracao = LocalDateTime.now();
}
