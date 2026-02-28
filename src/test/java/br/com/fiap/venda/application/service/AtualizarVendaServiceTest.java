package br.com.fiap.venda.application.service;

import br.com.fiap.venda.application.dto.AtualizarVendaCommand;
import br.com.fiap.venda.application.dto.StatusVeiculo;
import br.com.fiap.venda.application.dto.VeiculoSnapshot;
import br.com.fiap.venda.application.port.output.VeiculoConsultaPort;
import br.com.fiap.venda.application.port.output.VendaRepositoryPort;
import br.com.fiap.venda.domain.exception.VeiculoIndisponivelException;
import br.com.fiap.venda.domain.model.Venda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarVendaServiceTest {

    @Mock
    private VendaRepositoryPort repository;

    @Mock
    private VeiculoConsultaPort veiculoConsultaPort;

    @InjectMocks
    private AtualizarVendaService service;

    private UUID clienteId;
    private UUID veiculoId;
    private BigDecimal preco;
    private LocalDateTime dataVenda;
    private String statusPagamento;
    private AtualizarVendaCommand command;
    private Venda venda;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        veiculoId = UUID.randomUUID();
        preco = new BigDecimal(1);
        dataVenda = LocalDateTime.now();
        statusPagamento = "PAGO";
        command = new AtualizarVendaCommand(clienteId, veiculoId, preco, statusPagamento);
        venda = new Venda(clienteId, veiculoId, preco, dataVenda, statusPagamento);
    }

    @Test
    void executar_deveValidarEAtualizarStatusPagamento() {
        // arrange
        when(repository.buscarPorId(any(UUID.class), any(UUID.class)))
                .thenReturn(venda);

        when(veiculoConsultaPort.buscarPorId(veiculoId))
                .thenReturn(new VeiculoSnapshot(veiculoId, StatusVeiculo.VENDIDO));

        when(repository.salvar(any(Venda.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Venda> vendaCaptor = ArgumentCaptor.forClass(Venda.class);

        // act
        service.executar(command);

        // assert
        verify(veiculoConsultaPort).buscarPorId(veiculoId);

        verify(repository).salvar(vendaCaptor.capture());
        Venda vendaSalva = vendaCaptor.getValue();
        assertEquals(clienteId, vendaSalva.getClienteId());
        assertEquals(veiculoId, vendaSalva.getVeiculoId());
        assertEquals(preco, vendaSalva.getPreco());
        assertEquals(dataVenda, vendaSalva.getDataVenda());
        assertEquals(statusPagamento, vendaSalva.getStatusPagamento());

        verify(veiculoConsultaPort).buscarPorId(veiculoId);
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
