package com.dbserver.votacao.infrastructure.persistence.repository;

import com.dbserver.votacao.infrastructure.persistence.entity.PautaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaJpaRepository extends JpaRepository<PautaEntity, UUID> {}
