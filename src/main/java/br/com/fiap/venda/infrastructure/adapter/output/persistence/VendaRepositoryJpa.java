package br.com.fiap.venda.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepositoryJpa extends JpaRepository<VendaJpaEntity, VendaIdJpaEntity> {

}

