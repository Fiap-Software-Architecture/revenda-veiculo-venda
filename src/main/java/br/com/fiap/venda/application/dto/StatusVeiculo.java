package br.com.fiap.venda.application.dto;

import br.com.fiap.venda.domain.exception.DomainValidationException;

public enum StatusVeiculo {

    DISPONIVEL,
    INDISPONIVEL,
    RESERVADO,
    VENDIDO;

    public static StatusVeiculo from(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("veiculoId", "Status do veículo não informado pelo serviço de veículos");
        }
        try {
            return StatusVeiculo.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new DomainValidationException("veiculoId", "Status do veículo inválido: " + value);
        }
    }

}
