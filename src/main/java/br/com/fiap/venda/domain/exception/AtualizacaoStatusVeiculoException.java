package br.com.fiap.venda.domain.exception;

import java.util.UUID;

public class AtualizacaoStatusVeiculoException extends DomainException {
    public AtualizacaoStatusVeiculoException(UUID veiculoId, String detalhe) {
        super("Falha ao atualizar status do veículo " + veiculoId + ": " + detalhe);
    }
}
