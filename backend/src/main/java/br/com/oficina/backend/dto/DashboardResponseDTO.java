package br.com.oficina.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponseDTO(
    long totalClientes,
    long osAbertasOuEmAndamento,
    long osFinalizadas,
    BigDecimal faturamentoTotalFinalizado,
    BigDecimal custoPecasFinalizadas,
    BigDecimal lucroLiquidoReal,
    List<PecaResponseDTO> alertasEstoqueBaixo
) {}
