package com.dbserver.votacao.infrastructure.persistence.repository;

import com.dbserver.votacao.infrastructure.persistence.entity.SessaoVotacaoEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoVotacaoJpaRepository extends JpaRepository<SessaoVotacaoEntity, UUID> {

  Optional<SessaoVotacaoEntity> findByPautaId(UUID pautaId);
}
