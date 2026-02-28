package br.com.fiap.venda.infrastructure.adapter.input.rest;

import br.com.fiap.venda.application.dto.CadastrarVendaCommand;
import br.com.fiap.venda.application.dto.StatusVeiculo;
import br.com.fiap.venda.application.port.input.CadastrarVendaUseCase;
import br.com.fiap.venda.domain.exception.VeiculoIndisponivelException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(controllers = VendaController.class,
        excludeAutoConfiguration = {
                OAuth2ResourceServerAutoConfiguration.class
        }
)
class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarVendaUseCase cadastrarVendaUseCase;

    @Test
    void cadastrar_deveRetornar201() throws Exception {
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();

        String body = String.format("""
                {
                  "clienteId": "%s",
                  "veiculoId": "%s"
                }
                """, clienteId, veiculoId);

        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));

        verify(cadastrarVendaUseCase, times(1)).executar(any(CadastrarVendaCommand.class));
        verifyNoMoreInteractions(cadastrarVendaUseCase);
    }

    @Test
    void cadastrar_quandoBodyInvalido_deveRetornar400() throws Exception {
        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro de validação da requisição"));

        verifyNoInteractions(cadastrarVendaUseCase);
    }

    @Test
    void cadastrar_quandoVeiculoIndisponivel_deveRetornar409() throws Exception {
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();

        doThrow(new VeiculoIndisponivelException(veiculoId, StatusVeiculo.RESERVADO))
                .when(cadastrarVendaUseCase).executar(any(CadastrarVendaCommand.class));

        String body = String.format("""
                {
                  "clienteId": "%s",
                  "veiculoId": "%s"
                }
                """, clienteId, veiculoId);

        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        verify(cadastrarVendaUseCase, times(1)).executar(any(CadastrarVendaCommand.class));
        verifyNoMoreInteractions(cadastrarVendaUseCase);
    }
}
