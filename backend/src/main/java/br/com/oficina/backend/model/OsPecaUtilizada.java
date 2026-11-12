package br.com.oficina.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "os_pecas_utilizadas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OsPecaUtilizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peca_id", nullable = false)
    private Peca peca;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario_cobrado", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitarioCobrado;

    @Column(name = "custo_unitario_epoca", nullable = false, precision = 10, scale = 2)
    private BigDecimal custoUnitarioEpoca;
}
