package com.dbserver.votacao.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.dbserver.votacao.domain.enums.VotoValor;
import com.dbserver.votacao.domain.model.Voto;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class VotoTest {

  @Test
  void deveFalharQuandoCpfNaoTiver11Digitos() {
    final var agora = Instant.parse("2026-01-19T12:00:00Z");
    final var pautaId = UUID.randomUUID();

    final var ex1 =
        assertThrows(
            IllegalArgumentException.class,
            () -> new Voto(UUID.randomUUID(), pautaId, "123", VotoValor.SIM, agora));
    assertEquals("associadoCpf deve conter 11 dígitos", ex1.getMessage());

    final var ex2 =
        assertThrows(
            IllegalArgumentException.class,
            () -> new Voto(UUID.randomUUID(), pautaId, "123.456.789-0", VotoValor.SIM, agora));
    assertEquals("associadoCpf deve conter 11 dígitos", ex2.getMessage());
  }
}
