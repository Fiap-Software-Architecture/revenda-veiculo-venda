package br.com.fiap.venda.infrastructure.adapter.output.rest.dto;

import br.com.fiap.venda.application.dto.StatusVeiculo;
import br.com.fiap.venda.application.dto.VeiculoSnapshot;
import br.com.fiap.venda.domain.exception.AtualizacaoStatusVeiculoException;
import br.com.fiap.venda.domain.exception.DomainValidationException;
import br.com.fiap.venda.domain.exception.VeiculoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VeiculoRestClientAdapterTest {

    @Test
    void buscarPorId_deveRetornarSnapshotComStatusConvertido() {
        UUID veiculoId = UUID.randomUUID();

        RestClient restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);

        VeiculoResponse response = new VeiculoResponse();
        response.setId(veiculoId);
        response.setStatus("DISPONIVEL");

        when(restClient.get()
                .uri("/veiculos/{id}", veiculoId)
                .retrieve()
                .body(VeiculoResponse.class))
                .thenReturn(response);

        VeiculoRestClientAdapter adapter = new VeiculoRestClientAdapter(restClient);

        VeiculoSnapshot snapshot = adapter.buscarPorId(veiculoId);

        assertNotNull(snapshot);
        assertEquals(veiculoId, snapshot.id());
        assertEquals(StatusVeiculo.DISPONIVEL, snapshot.status());
    }

    @Test
    void buscarPorId_quandoRespostaNula_deveLancarDomainValidation() {
        UUID veiculoId = UUID.randomUUID();
        RestClient restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);

        when(restClient.get()
                .uri("/veiculos/{id}", veiculoId)
                .retrieve()
                .body(VeiculoResponse.class))
                .thenReturn(null);

        VeiculoRestClientAdapter adapter = new VeiculoRestClientAdapter(restClient);

        DomainValidationException ex = assertThrows(DomainValidationException.class, () -> adapter.buscarPorId(veiculoId));
        assertEquals("veiculoId", ex.getField());
        assertTrue(ex.getMessage().contains("Resposta vazia"));
    }

    @Test
    void buscarPorId_quando404_deveLancarVeiculoNaoEncontrado() {
        UUID veiculoId = UUID.randomUUID();
        RestClient restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);

        when(restClient.get()
                .uri("/veiculos/{id}", veiculoId)
                .retrieve()
                .body(VeiculoResponse.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        VeiculoRestClientAdapter adapter = new VeiculoRestClientAdapter(restClient);

        assertThrows(VeiculoNaoEncontradoException.class, () -> adapter.buscarPorId(veiculoId));
    }

    @Test
    void atualizarStatus_deveRealizarPatchSemErro() {
        UUID veiculoId = UUID.randomUUID();
        RestClient restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);

        when(restClient.patch()
                .uri("/veiculos/{id}", veiculoId)
                .body(any(AtualizarStatusVeiculoRequest.class))
                .retrieve()
                .toBodilessEntity())
                .thenReturn(ResponseEntity.ok().build());

        VeiculoRestClientAdapter adapter = new VeiculoRestClientAdapter(restClient);

        assertDoesNotThrow(() -> adapter.atualizarStatus(veiculoId, StatusVeiculo.VENDIDO));
    }

    @Test
    void atualizarStatus_quando404_deveLancarVeiculoNaoEncontrado() {
        UUID veiculoId = UUID.randomUUID();
        RestClient restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);

        when(restClient.patch()
                .uri("/veiculos/{id}", veiculoId)
                .body(any(AtualizarStatusVeiculoRequest.class))
                .retrieve()
                .toBodilessEntity())
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        VeiculoRestClientAdapter adapter = new VeiculoRestClientAdapter(restClient);

        assertThrows(VeiculoNaoEncontradoException.class, () -> adapter.atualizarStatus(veiculoId, StatusVeiculo.VENDIDO));
    }

    @Test
    void atualizarStatus_quandoErro4xxNao404_deveLancarAtualizacaoStatusVeiculoException() {
        UUID veiculoId = UUID.randomUUID();
        RestClient restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);

        when(restClient.patch()
                .uri("/veiculos/{id}", veiculoId)
                .body(any(AtualizarStatusVeiculoRequest.class))
                .retrieve()
                .toBodilessEntity())
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        VeiculoRestClientAdapter adapter = new VeiculoRestClientAdapter(restClient);

        assertThrows(AtualizacaoStatusVeiculoException.class, () -> adapter.atualizarStatus(veiculoId, StatusVeiculo.VENDIDO));
    }

    @Test
    void atualizarStatus_quandoErro5xxOuConexao_deveLancarAtualizacaoStatusVeiculoException() {
        UUID veiculoId = UUID.randomUUID();
        RestClient restClient = mock(RestClient.class, RETURNS_DEEP_STUBS);

        when(restClient.patch()
                .uri("/veiculos/{id}", veiculoId)
                .body(any(AtualizarStatusVeiculoRequest.class))
                .retrieve()
                .toBodilessEntity())
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        VeiculoRestClientAdapter adapter = new VeiculoRestClientAdapter(restClient);

        assertThrows(AtualizacaoStatusVeiculoException.class, () -> adapter.atualizarStatus(veiculoId, StatusVeiculo.VENDIDO));

        // ResourceAccessException também deve ser mapeada
        RestClient restClient2 = mock(RestClient.class, RETURNS_DEEP_STUBS);
        when(restClient2.patch()
                .uri("/veiculos/{id}", veiculoId)
                .body(any(AtualizarStatusVeiculoRequest.class))
                .retrieve()
                .toBodilessEntity())
                .thenThrow(new ResourceAccessException("timeout"));

        VeiculoRestClientAdapter adapter2 = new VeiculoRestClientAdapter(restClient2);
        assertThrows(AtualizacaoStatusVeiculoException.class, () -> adapter2.atualizarStatus(veiculoId, StatusVeiculo.VENDIDO));
    }
}
