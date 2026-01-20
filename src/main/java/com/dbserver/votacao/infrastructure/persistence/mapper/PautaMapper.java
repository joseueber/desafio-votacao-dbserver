package com.dbserver.votacao.infrastructure.persistence.mapper;

import com.dbserver.votacao.domain.model.Pauta;
import com.dbserver.votacao.infrastructure.persistence.entity.PautaEntity;

public class PautaMapper {

  public static PautaEntity toEntity(Pauta pauta) {
    PautaEntity e = new PautaEntity();
    e.setId(pauta.getId());
    e.setTitulo(pauta.getTitulo());
    e.setDescricao(pauta.getDescricao());
    e.setCriadaEm(pauta.getCriadaEm());
    return e;
  }

  public static Pauta toDomain(PautaEntity e) {
    return new Pauta(e.getId(), e.getTitulo(), e.getDescricao(), e.getCriadaEm());
  }
}
