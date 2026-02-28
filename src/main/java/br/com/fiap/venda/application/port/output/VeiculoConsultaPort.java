package br.com.fiap.venda.application.port.output;

import br.com.fiap.venda.application.dto.StatusVeiculo;
import br.com.fiap.venda.application.dto.VeiculoSnapshot;

import java.util.UUID;

public interface VeiculoConsultaPort {

    VeiculoSnapshot buscarPorId(UUID veiculoId);

    void atualizarStatus(UUID veiculoId, StatusVeiculo novoStatus);

}
