package br.com.fiap.venda.infrastructure.adapter.output.persistence;

import br.com.fiap.venda.application.dto.CadastrarVendaCommand;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaPersistenceAdapterTest {

    @Mock
    private VendaRepositoryJpa repositoryJpa;

    @InjectMocks
    private VendaPersistenceAdapter adapter;

    private UUID clienteId;
    private UUID veiculoId;
    private BigDecimal preco;
    private LocalDateTime dataVenda;
    private String statusPagamento;
    private CadastrarVendaCommand command;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        veiculoId = UUID.randomUUID();
        preco = new BigDecimal(1);
        dataVenda = LocalDateTime.now();
        statusPagamento = "PAGO";
        command = new CadastrarVendaCommand(clienteId, veiculoId, preco, dataVenda, statusPagamento);
    }

    @Test
    void salvar_deveConverterDominioParaEntityESalvar() {

        Venda venda = new Venda(clienteId, veiculoId, preco, dataVenda, statusPagamento);

        ArgumentCaptor<VendaJpaEntity> captor = ArgumentCaptor.forClass(VendaJpaEntity.class);

        when(repositoryJpa.save(any(VendaJpaEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Venda retorno = adapter.salvar(venda);

        verify(repositoryJpa).save(captor.capture());
        VendaJpaEntity entitySalva = captor.getValue();
        assertEquals(clienteId, getField(entitySalva, "clienteId"));
        assertEquals(veiculoId, getField(entitySalva, "veiculoId"));
        assertEquals(preco, getField(entitySalva, "preco"));
        assertEquals(dataVenda, getField(entitySalva, "dataVenda"));
        assertEquals(statusPagamento, getField(entitySalva, "statusPagamento"));

        assertNotNull(retorno);
        assertEquals(clienteId, retorno.getClienteId());
        assertEquals(veiculoId, retorno.getVeiculoId());

        verifyNoMoreInteractions(repositoryJpa);
    }

    private Object getField(Object target, String fieldName) {
        try {
            var f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
