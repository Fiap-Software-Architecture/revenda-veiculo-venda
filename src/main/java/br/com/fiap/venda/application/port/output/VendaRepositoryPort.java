package br.com.fiap.venda.application.port.output;

import br.com.fiap.venda.domain.model.Venda;

public interface VendaRepositoryPort {

    Venda salvar(Venda venda);

}
