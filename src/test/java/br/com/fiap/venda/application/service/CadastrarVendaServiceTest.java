package br.com.fiap.venda.application.service;

import br.com.fiap.venda.application.dto.CadastrarVendaCommand;
import br.com.fiap.venda.application.dto.StatusVeiculo;
import br.com.fiap.venda.application.dto.VeiculoSnapshot;
import br.com.fiap.venda.application.port.output.VendaRepositoryPort;
import br.com.fiap.venda.application.port.output.VeiculoConsultaPort;
import br.com.fiap.venda.domain.exception.VeiculoIndisponivelException;
import br.com.fiap.venda.domain.model.Venda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarVendaServiceTest {

    @Mock
    private VendaRepositoryPort repository;

    @Mock
    private VeiculoConsultaPort veiculoConsultaPort;

    @InjectMocks
    private CadastrarVendaService service;

    private UUID clienteId;
    private UUID veiculoId;
    private CadastrarVendaCommand command;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        veiculoId = UUID.randomUUID();
        command = new CadastrarVendaCommand(clienteId, veiculoId);
    }

    @Test
    void executar_deveReservarSalvarEAtualizarParaVendido() {
        // arrange
        when(veiculoConsultaPort.buscarPorId(veiculoId))
                .thenReturn(new VeiculoSnapshot(veiculoId, StatusVeiculo.DISPONIVEL));

        when(repository.salvar(any(Venda.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Venda> compraCaptor = ArgumentCaptor.forClass(Venda.class);

        // act
        service.executar(command);

        // assert
        verify(veiculoConsultaPort).buscarPorId(veiculoId);
        verify(veiculoConsultaPort).atualizarStatus(veiculoId, StatusVeiculo.RESERVADO);

        verify(repository).salvar(compraCaptor.capture());
        Venda vendaSalva = compraCaptor.getValue();
        assertEquals(clienteId, vendaSalva.getClienteId());
        assertEquals(veiculoId, vendaSalva.getVeiculoId());

        verify(veiculoConsultaPort).atualizarStatus(veiculoId, StatusVeiculo.VENDIDO);
        verifyNoMoreInteractions(repository, veiculoConsultaPort);
    }

    @Test
    void executar_quandoVeiculoNaoDisponivel_deveLancarExceptionENaoReservarNemSalvar() {
        // arrange
        when(veiculoConsultaPort.buscarPorId(veiculoId))
                .thenReturn(new VeiculoSnapshot(veiculoId, StatusVeiculo.RESERVADO));

        // act + assert
        assertThrows(VeiculoIndisponivelException.class, () -> service.executar(command));

        verify(veiculoConsultaPort).buscarPorId(veiculoId);
        verify(veiculoConsultaPort, never()).atualizarStatus(any(UUID.class), any(StatusVeiculo.class));
        verifyNoInteractions(repository);
        verifyNoMoreInteractions(veiculoConsultaPort);
    }
}
