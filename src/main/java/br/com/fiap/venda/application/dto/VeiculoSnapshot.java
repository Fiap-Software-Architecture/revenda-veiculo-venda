package br.com.fiap.venda.application.dto;

import java.util.UUID;

public record VeiculoSnapshot (
    UUID id,
    StatusVeiculo status
) {}
