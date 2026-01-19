package com.dbserver.votacao.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Getter
@Setter
@Entity
@Table(name = "sessao_votacao")
public class SessaoVotacaoEntity {

  @Id
  @Generated(GenerationTime.INSERT)
  @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
  @ColumnDefault("uuid_generate_v4()")
  private UUID id;

  @Column(name = "pauta_id", nullable = false, columnDefinition = "uuid")
  private UUID pautaId;

  @Column(name = "aberta_em", nullable = false)
  private Instant abertaEm;

  @Column(name = "fecha_em", nullable = false)
  private Instant fechaEm;

  @Column(nullable = false, length = 20)
  private String status;
}
