package br.com.oficina.backend.service;

import br.com.oficina.backend.dto.AdicionarPecaOsDTO;
import br.com.oficina.backend.enums.StatusOrdemServico;
import br.com.oficina.backend.model.*;
import br.com.oficina.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceTest {

    @Mock private OrdemServicoRepository osRepository;
    @Mock private EquipamentoRepository equipamentoRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private PecaRepository pecaRepository;
    @Mock private OsPecaUtilizadaRepository osPecaRepository;
    @Mock private OsHistoricoStatusRepository historicoRepository;

    @InjectMocks
    private OrdemServicoService osService;

    @Test
    void deveBaixarEstoqueECalcularTotalAoAdicionarPecaNaOs() {
        Cliente cliente = Cliente.builder().id(1L).nome("Carlos").cpf("12345678901").build();
        Equipamento equip = Equipamento.builder().id(1L).cliente(cliente).tipo("Notebook").marca("Dell").modelo("Inspiron").build();

        OrdemServico os = OrdemServico.builder()
                .id(1L)
                .protocolo("OS-2026-0001")
                .equipamento(equip)
                .status(StatusOrdemServico.EM_ANALISE)
                .valorMaoDeObra(new BigDecimal("100.00"))
                .valorTotalPecas(BigDecimal.ZERO)
                .valorTotal(new BigDecimal("100.00"))
                .build();

        Peca ssd = Peca.builder()
                .id(1L)
                .codigoSku("SSD-512")
                .nome("SSD 512GB")
                .quantidadeEstoque(5)
                .precoCusto(new BigDecimal("180.00"))
                .precoVenda(new BigDecimal("300.00"))
                .build();

        when(osRepository.findById(1L)).thenReturn(Optional.of(os));
        when(pecaRepository.findById(1L)).thenReturn(Optional.of(ssd));
        when(osRepository.save(any(OrdemServico.class))).thenAnswer(i -> i.getArgument(0));
        when(historicoRepository.findByOrdemServicoIdOrderByDataAlteracaoAsc(1L)).thenReturn(List.of());

        var resultado = osService.adicionarPeca(1L, new AdicionarPecaOsDTO(1L, 2));

        assertEquals(3, ssd.getQuantidadeEstoque());
        assertEquals(new BigDecimal("600.00"), resultado.valorTotalPecas());
        assertEquals(new BigDecimal("700.00"), resultado.valorTotal());
        verify(pecaRepository, times(1)).save(ssd);
        verify(osPecaRepository, times(1)).save(any(OsPecaUtilizada.class));
    }

    @Test
    void deveLancarErroQuandoEstoqueForInsuficiente() {
        OrdemServico os = OrdemServico.builder().id(1L).status(StatusOrdemServico.EM_ANALISE).build();
        Peca memoria = Peca.builder().id(1L).nome("Memória RAM 16GB").quantidadeEstoque(1).build();

        when(osRepository.findById(1L)).thenReturn(Optional.of(os));
        when(pecaRepository.findById(1L)).thenReturn(Optional.of(memoria));

        assertThrows(IllegalArgumentException.class, () -> osService.adicionarPeca(1L, new AdicionarPecaOsDTO(1L, 3)));
        verify(osPecaRepository, never()).save(any());
    }
}
