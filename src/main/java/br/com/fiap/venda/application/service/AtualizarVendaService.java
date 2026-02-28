package br.com.fiap.venda.application.service;

import br.com.fiap.venda.application.dto.AtualizarVendaCommand;
import br.com.fiap.venda.application.dto.StatusVeiculo;
import br.com.fiap.venda.application.dto.VeiculoSnapshot;
import br.com.fiap.venda.application.port.input.AtualizarVendaUseCase;
import br.com.fiap.venda.application.port.output.VeiculoConsultaPort;
import br.com.fiap.venda.application.port.output.VendaRepositoryPort;
import br.com.fiap.venda.domain.exception.VeiculoIndisponivelException;
import br.com.fiap.venda.domain.factory.VendaFactory;
import br.com.fiap.venda.domain.model.Venda;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AtualizarVendaService implements AtualizarVendaUseCase {

    private final VendaRepositoryPort repository;
    private final VeiculoConsultaPort veiculoConsultaPort;

    @Override
    public void executar(AtualizarVendaCommand command) {
        validaSeVeiculoEstaVendido(command);
        atualizaAVenda(command);
    }

    private void atualizaAVenda(AtualizarVendaCommand command) {

        Venda vendaRecuperada = repository.buscarPorId(command.clienteId(), command.veiculoId());

        Venda venda = VendaFactory.atualizaStatusPagamento(
                vendaRecuperada.getClienteId(),
                vendaRecuperada.getVeiculoId(),
                vendaRecuperada.getPreco(),
                vendaRecuperada.getDataVenda(),
                command.statusPagamento()
        );
        repository.salvar(venda);
    }

    private void validaSeVeiculoEstaVendido(AtualizarVendaCommand command) {
        VeiculoSnapshot veiculo = veiculoConsultaPort.buscarPorId(command.veiculoId());
        if (veiculo.status() != StatusVeiculo.VENDIDO) {
            throw new VeiculoIndisponivelException(command.veiculoId(), veiculo.status());
        }
    }

}

