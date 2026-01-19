package com.dbserver.votacao.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pauta")
public class PautaEntity {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @Column(nullable = false, length = 200)
  private String titulo;

  @Column(length = 1000)
  private String descricao;

  @Column(name = "criada_em", nullable = false)
  private Instant criadaEm;
}
