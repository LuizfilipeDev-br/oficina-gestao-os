package br.com.oficina.backend.model;

import br.com.oficina.backend.enums.StatusOrdemServico;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordens_servico")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String protocolo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusOrdemServico status;

    @Column(name = "defeito_relatado", nullable = false, columnDefinition = "TEXT")
    private String defeitoRelatado;

    @Column(name = "diagnostico_tecnico", columnDefinition = "TEXT")
    private String diagnosticoTecnico;

    @Column(name = "valor_mao_de_obra", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal valorMaoDeObra = BigDecimal.ZERO;

    @Column(name = "valor_total_pecas", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal valorTotalPecas = BigDecimal.ZERO;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "orcamento_aprovado", nullable = false)
    @Builder.Default
    private Boolean orcamentoAprovado = false;

    @Column(name = "data_abertura", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataAbertura = LocalDateTime.now();

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "data_finalizacao")
    private LocalDateTime dataFinalizacao;
}
