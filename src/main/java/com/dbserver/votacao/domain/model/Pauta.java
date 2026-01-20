package com.dbserver.votacao.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public final class Pauta {

  private final UUID id;
  private final String titulo;
  private final String descricao;
  private final Instant criadaEm;

  public Pauta(UUID id, String titulo, String descricao, Instant criadaEm) {
    this.id = id;
    this.titulo = validarTitulo(titulo);
    this.descricao = descricao;
    this.criadaEm = Objects.requireNonNull(criadaEm, "criadaEm não pode ser nulo");
  }

  public static Pauta nova(String titulo, String descricao, Instant criadaEm) {
    return new Pauta(null, titulo, descricao, criadaEm);
  }

  private static String validarTitulo(String titulo) {
    if (titulo == null || titulo.isBlank()) {
      throw new IllegalArgumentException("título não pode ser vazio");
    }
    if (titulo.length() > 200) {
      throw new IllegalArgumentException("título deve ter no máximo 200 caracteres");
    }
    return titulo;
  }
}
