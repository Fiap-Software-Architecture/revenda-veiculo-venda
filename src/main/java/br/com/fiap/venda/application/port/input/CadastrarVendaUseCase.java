package br.com.fiap.venda.application.port.input;

import br.com.fiap.venda.application.dto.CadastrarVendaCommand;

public interface CadastrarVendaUseCase {

    void executar(CadastrarVendaCommand command);

}
