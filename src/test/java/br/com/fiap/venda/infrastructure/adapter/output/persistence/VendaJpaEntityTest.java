package br.com.fiap.venda.infrastructure.adapter.output.persistence;

import br.com.fiap.venda.domain.model.Venda;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VendaJpaEntityTest {

    @Test
    void fromDomain_deveConverterCompraParaEntity() {
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();
        Venda venda = new Venda(clienteId, veiculoId);

        VendaJpaEntity entity = VendaJpaEntity.fromDomain(venda);

        assertNotNull(entity);
        assertEquals(clienteId, getField(entity, "clienteId"));
        assertEquals(veiculoId, getField(entity, "veiculoId"));
    }

    @Test
    void toDomain_deveConverterEntityParaCompra() {
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();
        VendaJpaEntity entity = new VendaJpaEntity(clienteId, veiculoId);

        Venda venda = entity.toDomain();

        assertNotNull(venda);
        assertEquals(clienteId, venda.getClienteId());
        assertEquals(veiculoId, venda.getVeiculoId());
    }

    private Object getField(Object target, String fieldName) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
