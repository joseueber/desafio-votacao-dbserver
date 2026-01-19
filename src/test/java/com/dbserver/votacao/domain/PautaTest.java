package com.dbserver.votacao.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PautaTest {

  @Test
  void deveFalharQuandoTituloNuloOuEmBranco() {
    final var agora = Instant.parse("2026-01-19T12:00:00Z");

    final var ex1 =
        assertThrows(
            IllegalArgumentException.class, () -> new Pauta(UUID.randomUUID(), null, null, agora));
    assertEquals("título não pode ser vazio", ex1.getMessage());

    final var ex2 =
        assertThrows(
            IllegalArgumentException.class, () -> new Pauta(UUID.randomUUID(), "   ", null, agora));
    assertEquals("título não pode ser vazio", ex2.getMessage());
  }

  @Test
  void deveFalharQuandoTituloMaiorQue200() {
    final var agora = Instant.parse("2026-01-19T12:00:00Z");
    final var titulo201 = "a".repeat(201);

    final var ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> new Pauta(UUID.randomUUID(), titulo201, null, agora));

    assertEquals("título deve ter no máximo 200 caracteres", ex.getMessage());
  }
}
