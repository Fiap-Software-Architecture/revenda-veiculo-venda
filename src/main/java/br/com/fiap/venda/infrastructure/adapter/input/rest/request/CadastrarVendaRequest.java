package br.com.fiap.venda.infrastructure.adapter.input.rest.request;

import br.com.fiap.venda.application.dto.CadastrarVendaCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CadastrarVendaRequest {

    public static final String STATUS_PGTO_PENDENTE = "PENDENTE";
    @NotNull
    private UUID clienteId;

    @NotNull
    private UUID veiculoId;

    @NotNull
    private BigDecimal preco;

    @NotNull
    private LocalDateTime dataVenda;

    public CadastrarVendaCommand toCommand() {
        return new CadastrarVendaCommand(
                clienteId,
                veiculoId,
                preco,
                dataVenda,
                STATUS_PGTO_PENDENTE
        );
    }

}

