package br.com.fiap.venda.infrastructure.adapter.input.rest.request;

import br.com.fiap.venda.application.dto.AtualizarVendaCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AtualizarStatusPagamentoRequest {

    @NotNull
    private UUID clienteId;

    @NotNull
    private UUID veiculoId;

    @NotNull
    private BigDecimal valor;

    @NotNull
    private String statusPagamento;

    public AtualizarVendaCommand toCommand() {
        return new AtualizarVendaCommand(
                clienteId,
                veiculoId,
                valor,
                statusPagamento
        );
    }

}

