package br.com.fiap.venda.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Venda {

    private final UUID clienteId;
    private final UUID veiculoId;
    private final BigDecimal preco;
    private final LocalDateTime dataVenda;
    private final String statusPagamento;
    
}

