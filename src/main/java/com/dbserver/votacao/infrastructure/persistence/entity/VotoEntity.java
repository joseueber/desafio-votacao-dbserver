package com.dbserver.votacao.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "voto")
public class VotoEntity {

  @Id
  @Column(columnDefinition = "uuid")
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
