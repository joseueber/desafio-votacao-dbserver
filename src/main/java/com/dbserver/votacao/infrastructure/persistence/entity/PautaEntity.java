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
@Table(name = "pauta")
public class PautaEntity {

  @Id
  @Generated(GenerationTime.INSERT)
  @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
  @ColumnDefault("uuid_generate_v4()")
  private UUID id;

  @Column(nullable = false, length = 200)
  private String titulo;

  @Column(length = 1000)
  private String descricao;

  @Column(name = "criada_em", nullable = false)
  private Instant criadaEm;
}
