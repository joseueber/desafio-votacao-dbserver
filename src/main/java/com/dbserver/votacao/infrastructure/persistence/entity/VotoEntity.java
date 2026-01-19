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
@Table(name = "voto")
public class VotoEntity {

  @Id
  @Generated(GenerationTime.INSERT)
  @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
  @ColumnDefault("uuid_generate_v4()")
  private UUID id;

  @Column(name = "pauta_id", nullable = false, columnDefinition = "uuid")
  private UUID pautaId;

  @Column(name = "associado_cpf", nullable = false, length = 11)
  private String associadoCpf;

  @Column(nullable = false, length = 3)
  private String valor;

  @Column(name = "criado_em", nullable = false)
  private Instant criadoEm;
}
