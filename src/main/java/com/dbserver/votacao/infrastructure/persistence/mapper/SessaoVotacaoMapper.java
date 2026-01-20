package com.dbserver.votacao.infrastructure.persistence.mapper;

import com.dbserver.votacao.domain.model.SessaoVotacao;
import com.dbserver.votacao.domain.enums.SessaoStatus;
import com.dbserver.votacao.infrastructure.persistence.entity.SessaoVotacaoEntity;

public class SessaoVotacaoMapper {

  public static SessaoVotacaoEntity toEntity(SessaoVotacao s) {
    SessaoVotacaoEntity e = new SessaoVotacaoEntity();
    e.setId(s.getId());
    e.setPautaId(s.getPautaId());
    e.setAbertaEm(s.getAbertaEm());
    e.setFechaEm(s.getFechaEm());
    e.setStatus(s.getStatus().name());
    return e;
  }

  public static SessaoVotacao toDomain(SessaoVotacaoEntity e) {
    return new SessaoVotacao(
        e.getId(),
        e.getPautaId(),
        e.getAbertaEm(),
        e.getFechaEm(),
        SessaoStatus.valueOf(e.getStatus()));
  }
}
