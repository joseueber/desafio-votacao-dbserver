package com.dbserver.votacao.infrastructure.persistence.repository;

import com.dbserver.votacao.infrastructure.persistence.entity.VotoEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VotoJpaRepository extends JpaRepository<VotoEntity, UUID> {

  boolean existsByPautaIdAndAssociadoCpf(UUID pautaId, String associadoCpf);

  @Query(
      """
      select v.valor, count(v)
      from VotoEntity v
      where v.pautaId = :pautaId
      group by v.valor
      """)
  java.util.List<Object[]> contarPorValor(UUID pautaId);
}
