package br.com.fiap.venda.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Venda {

    private final UUID clienteId;
    private final UUID veiculoId;
    
}

