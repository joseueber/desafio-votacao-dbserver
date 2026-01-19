package com.dbserver.votacao.domain;

import com.dbserver.votacao.domain.enums.SessaoStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public final class SessaoVotacao {

  private final UUID id;
  private final UUID pautaId;
  private final Instant abertaEm;
  private final Instant fechaEm;
  private final SessaoStatus status;

  public SessaoVotacao(
      UUID id, UUID pautaId, Instant abertaEm, Instant fechaEm, SessaoStatus status) {
    this.id = Objects.requireNonNull(id, "id não pode ser nulo");
    this.pautaId = Objects.requireNonNull(pautaId, "pautaId não pode ser nulo");
    this.abertaEm = Objects.requireNonNull(abertaEm, "abertaEm não pode ser nulo");
    this.fechaEm = Objects.requireNonNull(fechaEm, "fechaEm não pode ser nulo");
    this.status = Objects.requireNonNull(status, "status não pode ser nulo");
    if (!fechaEm.isAfter(abertaEm)) {
      throw new IllegalArgumentException("fechaEm deve ser depois de abertaEm");
    }
  }

  public static SessaoVotacao abrir(UUID id, UUID pautaId, Instant agora, Duration duracao) {
    Objects.requireNonNull(agora, "agora não pode ser nulo");
    if (duracao == null || duracao.isZero() || duracao.isNegative()) {
      duracao = Duration.ofMinutes(1);
    }
    return new SessaoVotacao(id, pautaId, agora, agora.plus(duracao), SessaoStatus.ABERTA);
  }

  public boolean estaAbertaEm(Instant agora) {
    Objects.requireNonNull(agora, "agora não pode ser nulo");
    return status == SessaoStatus.ABERTA && agora.isBefore(fechaEm);
  }

  public SessaoVotacao encerrar() {
    if (status == SessaoStatus.ENCERRADA) {
      return this;
    }
    return new SessaoVotacao(id, pautaId, abertaEm, fechaEm, SessaoStatus.ENCERRADA);
  }
}
