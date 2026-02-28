package br.com.fiap.venda.application.service;

import br.com.fiap.venda.application.dto.CadastrarVendaCommand;
import br.com.fiap.venda.application.dto.StatusVeiculo;
import br.com.fiap.venda.application.dto.VeiculoSnapshot;
import br.com.fiap.venda.application.port.input.CadastrarVendaUseCase;
import br.com.fiap.venda.application.port.output.VendaRepositoryPort;
import br.com.fiap.venda.application.port.output.VeiculoConsultaPort;
import br.com.fiap.venda.domain.exception.VeiculoIndisponivelException;
import br.com.fiap.venda.domain.factory.VendaFactory;
import br.com.fiap.venda.domain.model.Venda;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CadastrarVendaService implements CadastrarVendaUseCase {

    private final VendaRepositoryPort repository;
    private final VeiculoConsultaPort veiculoConsultaPort;

    @Override
    public void executar(CadastrarVendaCommand command) {
        validaSeVeiculoEstaDisponivel(command);
        reservaVeiculo(command);
        realizaAVenda(command);
        atualizaVeiculoParaVendido(command);
    }

    private void realizaAVenda(CadastrarVendaCommand command) {
        Venda venda = VendaFactory.novoCadastro(
                command.clienteId(),
                command.veiculoId(),
                command.preco(),
                command.dataVenda(),
                command.statusPagamento()
        );
        repository.salvar(venda);
    }

    private void atualizaVeiculoParaVendido(CadastrarVendaCommand command) {
        veiculoConsultaPort.atualizarStatus(command.veiculoId(), StatusVeiculo.VENDIDO);
    }

    private void reservaVeiculo(CadastrarVendaCommand command) {
        veiculoConsultaPort.atualizarStatus(command.veiculoId(), StatusVeiculo.RESERVADO);
    }

    private void validaSeVeiculoEstaDisponivel(CadastrarVendaCommand command) {
        VeiculoSnapshot veiculo = veiculoConsultaPort.buscarPorId(command.veiculoId());
        if (veiculo.status() != StatusVeiculo.DISPONIVEL) {
            throw new VeiculoIndisponivelException(command.veiculoId(), veiculo.status());
        }
    }
}

