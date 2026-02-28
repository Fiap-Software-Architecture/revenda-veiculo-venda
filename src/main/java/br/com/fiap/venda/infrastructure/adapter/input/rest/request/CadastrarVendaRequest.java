package br.com.fiap.venda.infrastructure.adapter.input.rest.request;

import br.com.fiap.venda.application.dto.CadastrarVendaCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class CadastrarVendaRequest {

    @NotNull
    private UUID clienteId;

    @NotNull
    private UUID veiculoId;

    public CadastrarVendaCommand toCommand() {
        return new CadastrarVendaCommand(
                clienteId,
                veiculoId
        );
    }

}

