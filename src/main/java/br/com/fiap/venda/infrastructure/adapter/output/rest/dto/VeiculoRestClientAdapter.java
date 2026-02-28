package br.com.fiap.venda.infrastructure.adapter.output.rest.dto;

import br.com.fiap.venda.application.dto.StatusVeiculo;
import br.com.fiap.venda.application.dto.VeiculoSnapshot;
import br.com.fiap.venda.application.port.output.VeiculoConsultaPort;
import br.com.fiap.venda.domain.exception.AtualizacaoStatusVeiculoException;
import br.com.fiap.venda.domain.exception.DomainValidationException;
import br.com.fiap.venda.domain.exception.VeiculoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VeiculoRestClientAdapter implements VeiculoConsultaPort {

    private final RestClient veiculoRestClient;

    @Override
    public VeiculoSnapshot buscarPorId(UUID veiculoId) {
        try {
            VeiculoResponse response = veiculoRestClient
                    .get()
                    .uri("/veiculos/{id}", veiculoId)
                    .retrieve()
                    .body(VeiculoResponse.class);

            if (response == null) {
                throw new DomainValidationException("veiculoId", "Resposta vazia do serviço de veículos");
            }

            StatusVeiculo status = StatusVeiculo.from(response.getStatus());
            return new VeiculoSnapshot(response.getId(), status);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new VeiculoNaoEncontradoException(veiculoId);
            }
            throw e;
        }
    }

    @Override
    public void atualizarStatus(UUID veiculoId, StatusVeiculo novoStatus) {
        try {
            veiculoRestClient
                    .patch()
                    .uri("/veiculos/{id}", veiculoId)
                    .body(new AtualizarStatusVeiculoRequest(novoStatus.name()))
                    .retrieve()
                    .toBodilessEntity();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new VeiculoNaoEncontradoException(veiculoId);
            }
            throw new AtualizacaoStatusVeiculoException(veiculoId, "Erro HTTP " + e.getStatusCode());
        } catch (HttpServerErrorException | ResourceAccessException e) {
            throw new AtualizacaoStatusVeiculoException(veiculoId, e.getMessage());
        }
    }

}
