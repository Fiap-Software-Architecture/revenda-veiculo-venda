package br.com.fiap.venda.application.port.input;

import br.com.fiap.venda.application.dto.AtualizarVendaCommand;

public interface AtualizarVendaUseCase {

    void executar(AtualizarVendaCommand command);

}
