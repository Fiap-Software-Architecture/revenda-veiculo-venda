package br.com.fiap.venda.domain.factory;

import br.com.fiap.venda.domain.exception.DomainValidationException;
import br.com.fiap.venda.domain.model.Venda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class VendaFactory {

    public static Venda novoCadastro(UUID clienteId, UUID veiculoId, BigDecimal preco, LocalDateTime dataVenda, String statusPagamento) {
        validarDadosPadroes(clienteId, veiculoId);

        return new Venda(
                clienteId,
                veiculoId,
                preco,
                dataVenda,
                statusPagamento
        );
    }

    public static Venda atualizaStatusPagamento(UUID clienteId, UUID veiculoId, BigDecimal preco, LocalDateTime dataVenda, String statusPagamento) {
        validarDadosPadroes(clienteId, veiculoId);

        return new Venda(
                clienteId,
                veiculoId,
                preco,
                dataVenda,
                statusPagamento
        );
    }

    private static void validarDadosPadroes(UUID clienteId, UUID veiculoId) {
        if (clienteId == null) throw new DomainValidationException("clienteId", "clienteId é obrigatório");
        if (veiculoId == null) throw new DomainValidationException("veiculoId", "veiculoId é obrigatório");
    }

}
