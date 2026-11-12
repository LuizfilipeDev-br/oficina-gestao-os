package br.com.oficina.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipamentos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(name = "numero_serie", unique = true, length = 100)
    private String numeroSerie;

    @Column(name = "especificacoes_hardware", columnDefinition = "TEXT")
    private String especificacoesHardware;

    @Column(name = "observacoes_fisicas", columnDefinition = "TEXT")
    private String observacoesFisicas;
}
