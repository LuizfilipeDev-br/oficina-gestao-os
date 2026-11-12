package br.com.oficina.backend.dto;

import br.com.oficina.backend.model.Peca;
import java.math.BigDecimal;

public record PecaResponseDTO(
    Long id, String codigoSku, String nome, String categoria, Integer quantidadeEstoque,
    Integer estoqueMinimo, boolean estoqueBaixo, BigDecimal precoCusto, BigDecimal precoVenda
) {
    public static PecaResponseDTO fromEntity(Peca p) {
        boolean alerta = p.getQuantidadeEstoque() <= p.getEstoqueMinimo();
        return new PecaResponseDTO(
            p.getId(), p.getCodigoSku(), p.getNome(), p.getCategoria(), p.getQuantidadeEstoque(),
            p.getEstoqueMinimo(), alerta, p.getPrecoCusto(), p.getPrecoVenda()
        );
    }
}
