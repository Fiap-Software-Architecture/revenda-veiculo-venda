package br.com.fiap.venda.domain.exception;

import java.util.UUID;

public class VeiculoNaoEncontradoException extends DomainException {
    public VeiculoNaoEncontradoException(UUID veiculoId) {
        super("Veículo não encontrado para o id: " + veiculoId);
    }
}
