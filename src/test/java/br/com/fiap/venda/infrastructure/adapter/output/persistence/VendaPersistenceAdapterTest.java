package br.com.fiap.venda.infrastructure.adapter.output.persistence;

import br.com.fiap.venda.domain.model.Venda;
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
class VendaPersistenceAdapterTest {

    @Mock
    private VendaRepositoryJpa repositoryJpa;

    @InjectMocks
    private VendaPersistenceAdapter adapter;

    @Test
    void salvar_deveConverterDominioParaEntityESalvar() {
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();
        Venda venda = new Venda(clienteId, veiculoId);

        ArgumentCaptor<VendaJpaEntity> captor = ArgumentCaptor.forClass(VendaJpaEntity.class);

        when(repositoryJpa.save(any(VendaJpaEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Venda retorno = adapter.salvar(venda);

        verify(repositoryJpa).save(captor.capture());
        VendaJpaEntity entitySalva = captor.getValue();
        assertEquals(clienteId, getField(entitySalva, "clienteId"));
        assertEquals(veiculoId, getField(entitySalva, "veiculoId"));

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
