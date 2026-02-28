package br.com.fiap.venda.infrastructure.adapter.output.persistence;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class VendaIdJpaEntity implements Serializable {

    private UUID clienteId;
    private UUID veiculoId;
}
