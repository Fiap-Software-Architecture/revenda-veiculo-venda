package br.com.fiap.venda.infrastructure.adapter.output.persistence;

import br.com.fiap.venda.domain.model.Venda;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VendaJpaEntityTest {

    private static final BigDecimal PRECO = new BigDecimal(1);
    private static final String STATUS_PAGAMENTO = "PAGO";

    @Test
    void fromDomain_deveConverterVendaParaEntity() {

        LocalDateTime dataVenda = LocalDateTime.now();

        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();
        Venda venda = new Venda(clienteId, veiculoId, PRECO, dataVenda, STATUS_PAGAMENTO);

        VendaJpaEntity entity = VendaJpaEntity.fromDomain(venda);

        assertNotNull(entity);
        assertEquals(clienteId, getField(entity, "clienteId"));
        assertEquals(veiculoId, getField(entity, "veiculoId"));
        assertEquals(PRECO, getField(entity, "preco"));
        assertEquals(dataVenda, getField(entity, "dataVenda"));
        assertEquals(STATUS_PAGAMENTO, getField(entity, "statusPagamento"));
    }

    @Test
    void toDomain_deveConverterEntityParaVenda() {

        LocalDateTime dataVenda = LocalDateTime.now();

        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();
        VendaJpaEntity entity = new VendaJpaEntity(clienteId, veiculoId, PRECO, dataVenda, STATUS_PAGAMENTO);

        Venda venda = entity.toDomain();

        assertNotNull(venda);
        assertEquals(clienteId, venda.getClienteId());
        assertEquals(veiculoId, venda.getVeiculoId());
        assertEquals(PRECO, getField(entity, "preco"));
        assertEquals(dataVenda, getField(entity, "dataVenda"));
        assertEquals(STATUS_PAGAMENTO, getField(entity, "statusPagamento"));
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
