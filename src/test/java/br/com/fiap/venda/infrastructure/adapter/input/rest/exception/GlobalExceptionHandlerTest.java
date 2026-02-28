package br.com.fiap.venda.infrastructure.adapter.input.rest.exception;

import br.com.fiap.venda.domain.exception.AtualizacaoStatusVeiculoException;
import br.com.fiap.venda.domain.exception.DomainValidationException;
import br.com.fiap.venda.domain.exception.VeiculoIndisponivelException;
import br.com.fiap.venda.domain.exception.VeiculoNaoEncontradoException;
import br.com.fiap.venda.application.dto.StatusVeiculo;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.InputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidation_deveRetornar400ComFieldErrors() {
        // arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/compras");

        Object target = new Object();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "cadastrarCompraRequest");
        bindingResult.addError(new FieldError("cadastrarCompraRequest", "clienteId", "não pode ser nulo"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // act
        ResponseEntity<ApiErrorResponse> response = handler.handleBeanValidation(ex, request);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Erro de validação da requisição", body.message());
        assertEquals("/compras", body.path());
        assertEquals(1, body.fieldErrors().size());
        assertEquals("clienteId", body.fieldErrors().getFirst().field());
        assertEquals("não pode ser nulo", body.fieldErrors().getFirst().message());
    }

    @Test
    void handleDomainValidation_deveRetornar400() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/compras");

        DomainValidationException ex = new DomainValidationException("clienteId", "clienteId é obrigatório");

        ResponseEntity<ApiErrorResponse> response = handler.handleDomainValidation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Erro de validação no domínio", body.message());
        assertEquals(1, body.fieldErrors().size());
        assertEquals("clienteId", body.fieldErrors().getFirst().field());
        assertEquals("clienteId é obrigatório", body.fieldErrors().getFirst().message());
    }

    @Test
    void handleInvalidJson_deveRetornar400() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/compras");

        HttpInputMessage inputMessage = new HttpInputMessage() {
            @Override public InputStream getBody() { return InputStream.nullInputStream(); }
            @Override public HttpHeaders getHeaders() { return new HttpHeaders(); }
        };

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("invalid", inputMessage);

        ResponseEntity<ApiErrorResponse> response = handler.handleInvalidJson(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("JSON inválido ou malformado", body.message());
        assertEquals("/compras", body.path());
    }

    @Test
    void handleVeiculoNaoEncontrado_deveRetornar404() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/compras");

        UUID veiculoId = UUID.randomUUID();
        VeiculoNaoEncontradoException ex = new VeiculoNaoEncontradoException(veiculoId);

        ResponseEntity<ApiErrorResponse> response = handler.handleVeiculoNaoEncontrado(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertTrue(response.getBody().message().contains(veiculoId.toString()));
    }

    @Test
    void handleVeiculoIndisponivel_deveRetornar409() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/compras");

        UUID veiculoId = UUID.randomUUID();
        VeiculoIndisponivelException ex = new VeiculoIndisponivelException(veiculoId, StatusVeiculo.RESERVADO);

        ResponseEntity<ApiErrorResponse> response = handler.handleVeiculoIndisponivel(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().status());
        assertTrue(response.getBody().message().contains("não está DISPONIVEL"));
    }

    @Test
    void handleAtualizacaoStatusVeiculo_deveRetornar503() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/compras");

        UUID veiculoId = UUID.randomUUID();
        AtualizacaoStatusVeiculoException ex = new AtualizacaoStatusVeiculoException(veiculoId, "timeout");

        ResponseEntity<ApiErrorResponse> response = handler.handleAtualizacaoStatusVeiculo(ex, request);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(503, response.getBody().status());
        assertTrue(response.getBody().message().contains("Falha ao atualizar status"));
    }
}
