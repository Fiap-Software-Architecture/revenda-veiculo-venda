package br.com.fiap.venda.domain.factory;

import br.com.fiap.venda.domain.exception.DomainValidationException;
import br.com.fiap.venda.domain.model.Venda;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VendaFactoryTest {

    @Test
    void novoCadastro_deveCriarCompraComIdsInformados() {
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();

        Venda venda = VendaFactory.novoCadastro(clienteId, veiculoId);

        assertNotNull(venda);
        assertEquals(clienteId, venda.getClienteId());
        assertEquals(veiculoId, venda.getVeiculoId());
    }

    @Test
    void novoCadastro_quandoClienteIdNulo_deveLancarDomainValidation() {
        UUID veiculoId = UUID.randomUUID();

        DomainValidationException ex = assertThrows(DomainValidationException.class,
                () -> VendaFactory.novoCadastro(null, veiculoId));

        assertEquals("clienteId", ex.getField());
        assertEquals("clienteId é obrigatório", ex.getMessage());
    }

    @Test
    void novoCadastro_quandoVeiculoIdNulo_deveLancarDomainValidation() {
        UUID clienteId = UUID.randomUUID();

        DomainValidationException ex = assertThrows(DomainValidationException.class,
                () -> VendaFactory.novoCadastro(clienteId, null));

        assertEquals("veiculoId", ex.getField());
        assertEquals("veiculoId é obrigatório", ex.getMessage());
    }
}
