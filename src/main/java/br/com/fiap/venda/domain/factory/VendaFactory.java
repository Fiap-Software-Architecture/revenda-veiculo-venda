package br.com.fiap.venda.domain.factory;

import br.com.fiap.venda.domain.exception.DomainValidationException;
import br.com.fiap.venda.domain.model.Venda;

import java.util.UUID;

public class VendaFactory {

    public static Venda novoCadastro(UUID clienteId, UUID veiculoId) {
        validarDadosPadroes(clienteId, veiculoId);

        return new Venda(
                clienteId,
                veiculoId
        );
    }

    private static void validarDadosPadroes(UUID clienteId, UUID veiculoId) {
        if (clienteId == null) throw new DomainValidationException("clienteId", "clienteId é obrigatório");
        if (veiculoId == null) throw new DomainValidationException("veiculoId", "veiculoId é obrigatório");
    }

}
