package br.com.fiap.venda.application.dto;

import java.util.UUID;

public record CadastrarVendaCommand(
        UUID clienteId,
        UUID veiculoId
) {}
