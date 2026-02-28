package br.com.fiap.venda.domain.factory;

import br.com.fiap.venda.application.dto.CadastrarVendaCommand;
import br.com.fiap.venda.domain.exception.DomainValidationException;
import br.com.fiap.venda.domain.model.Venda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VendaFactoryTest {

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
    void novoCadastro_deveCriarVendaComIdsInformados() {
        Venda venda = VendaFactory.novoCadastro(clienteId, veiculoId, preco, dataVenda, statusPagamento);

        assertNotNull(venda);
        assertEquals(clienteId, venda.getClienteId());
        assertEquals(veiculoId, venda.getVeiculoId());
        assertEquals(preco, venda.getPreco());
        assertEquals(dataVenda, venda.getDataVenda());
        assertEquals(statusPagamento, venda.getStatusPagamento());
    }

    @Test
    void novoCadastro_quandoClienteIdNulo_deveLancarDomainValidation() {
        DomainValidationException ex = assertThrows(DomainValidationException.class,
                () -> VendaFactory.novoCadastro(null, veiculoId, preco, dataVenda, statusPagamento));

        assertEquals("clienteId", ex.getField());
        assertEquals("clienteId é obrigatório", ex.getMessage());
    }

    @Test
    void novoCadastro_quandoVeiculoIdNulo_deveLancarDomainValidation() {
        DomainValidationException ex = assertThrows(DomainValidationException.class,
                () -> VendaFactory.novoCadastro(clienteId, null, preco, dataVenda, statusPagamento));

        assertEquals("veiculoId", ex.getField());
        assertEquals("veiculoId é obrigatório", ex.getMessage());
    }
}
