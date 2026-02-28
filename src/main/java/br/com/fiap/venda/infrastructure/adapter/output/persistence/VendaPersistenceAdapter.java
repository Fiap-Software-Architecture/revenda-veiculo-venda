package br.com.fiap.venda.infrastructure.adapter.output.persistence;

import br.com.fiap.venda.application.port.output.VendaRepositoryPort;
import br.com.fiap.venda.domain.model.Venda;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class VendaPersistenceAdapter implements VendaRepositoryPort {

    private final VendaRepositoryJpa repository;

    @Override
    public Venda salvar(Venda venda) {
        VendaJpaEntity entity = VendaJpaEntity.fromDomain(venda);
        VendaJpaEntity entitySalva = repository.save(entity);
        return entitySalva.toDomain();
    }

    @Override
    public Venda buscarPorId(UUID clienteId, UUID veiculoId) {
        VendaIdJpaEntity id = new VendaIdJpaEntity(clienteId, veiculoId);
        VendaJpaEntity entity = repository.findById(id).orElseThrow();
        return entity.toDomain();
    }

}

