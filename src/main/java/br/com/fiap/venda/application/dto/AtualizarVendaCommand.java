package br.com.fiap.venda.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AtualizarVendaCommand(
        UUID clienteId,
        UUID veiculoId,
        BigDecimal valor,
        String statusPagamento
) {}
