package br.com.fiap.venda.infrastructure.adapter.output.persistence;

import br.com.fiap.venda.domain.model.Venda;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vendas")
@IdClass(VendaIdJpaEntity.class)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class VendaJpaEntity {

    @Id
    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Id
    @Column(name = "veiculo_id", nullable = false)
    private UUID veiculoId;

    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda;

    @Column(name = "status_pagamento", nullable = true)
    private String statusPagamento;

    public static VendaJpaEntity fromDomain(Venda venda) {
        VendaJpaEntity entity = new VendaJpaEntity();
        entity.clienteId = venda.getClienteId();
        entity.veiculoId = venda.getVeiculoId();
        entity.preco = venda.getPreco();
        entity.dataVenda = venda.getDataVenda();
        entity.statusPagamento = venda.getStatusPagamento();
        return entity;
    }

    public Venda toDomain() {
        return new Venda(
                this.clienteId,
                this.veiculoId,
                this.preco,
                this.dataVenda,
                this.statusPagamento
        );
    }
}

