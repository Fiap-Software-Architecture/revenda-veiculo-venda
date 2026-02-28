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

import java.util.UUID;

@Entity
@Table(name = "compras")
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

    public static VendaJpaEntity fromDomain(Venda venda) {
        VendaJpaEntity entity = new VendaJpaEntity();
        entity.clienteId = venda.getClienteId();
        entity.veiculoId = venda.getVeiculoId();
        return entity;
    }

    public Venda toDomain() {
        return new Venda(
                this.clienteId,
                this.veiculoId
        );
    }
}

