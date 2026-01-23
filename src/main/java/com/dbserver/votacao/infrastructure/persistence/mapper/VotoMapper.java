package com.dbserver.votacao.infrastructure.persistence.mapper;

import com.dbserver.votacao.domain.enums.VotoValor;
import com.dbserver.votacao.domain.model.Voto;
import com.dbserver.votacao.infrastructure.persistence.entity.VotoEntity;

public class VotoMapper {

  public static VotoEntity toEntity(Voto v) {
    VotoEntity e = new VotoEntity();
    e.setId(v.getId());
    e.setPautaId(v.getPautaId());
    e.setAssociadoCpf(v.getAssociadoCpf());
    e.setValor(v.getValor().name());
    e.setCriadoEm(v.getCriadoEm());
    return e;
  }

  public static Voto toDomain(VotoEntity e) {
    return new Voto(
        e.getId(),
        e.getPautaId(),
        e.getAssociadoCpf(),
        VotoValor.valueOf(e.getValor()),
        e.getCriadoEm());
  }
}
