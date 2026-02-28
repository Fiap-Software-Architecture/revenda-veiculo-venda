package br.com.fiap.venda.domain.exception;

import br.com.fiap.venda.application.dto.StatusVeiculo;

import java.util.UUID;

public class VeiculoIndisponivelException extends DomainException {
    public VeiculoIndisponivelException(UUID veiculoId, StatusVeiculo status) {
        super("Veículo " + veiculoId + " não está DISPONIVEL. Status atual: " + status);
    }
}
