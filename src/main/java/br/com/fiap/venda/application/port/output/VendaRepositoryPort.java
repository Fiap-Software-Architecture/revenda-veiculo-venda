package br.com.fiap.venda.application.port.output;

import br.com.fiap.venda.domain.model.Venda;

import java.util.UUID;

public interface VendaRepositoryPort {

    Venda salvar(Venda venda);

    Venda buscarPorId(UUID clienteId, UUID veiculoId);

}
