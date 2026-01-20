package com.dbserver.votacao.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.dbserver.votacao.domain.enums.SessaoStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import com.dbserver.votacao.domain.model.SessaoVotacao;
import org.junit.jupiter.api.Test;

class SessaoVotacaoTest {

  @Test
  void deveFalharQuandoFechaEmNaoForDepoisDeAbertaEm() {
    final var pautaId = UUID.randomUUID();
    final var t = Instant.parse("2026-01-19T12:00:00Z");

    final var ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> new SessaoVotacao(UUID.randomUUID(), pautaId, t, t, SessaoStatus.ABERTA));

    assertEquals("fechaEm deve ser depois de abertaEm", ex.getMessage());
  }

  @Test
  void deveUsarDuracaoPadraoQuandoDuracaoNulaOuInvalida() {
    final var pautaId = UUID.randomUUID();
    final var agora = Instant.parse("2026-01-19T12:00:00Z");

    final var s1 = SessaoVotacao.abrir(pautaId, agora, null);
    assertEquals(agora.plus(Duration.ofMinutes(1)), s1.getFechaEm());

    final var s2 = SessaoVotacao.abrir(pautaId, agora, Duration.ZERO);
    assertEquals(agora.plus(Duration.ofMinutes(1)), s2.getFechaEm());

    final var s3 = SessaoVotacao.abrir(pautaId, agora, Duration.ofSeconds(-1));
    assertEquals(agora.plus(Duration.ofMinutes(1)), s3.getFechaEm());
  }

  @Test
  void estaAbertaEmDeveRetornarTrueApenasQuandoStatusAbertaEAindaNaoChegouNoFechamento() {
    final var pautaId = UUID.randomUUID();
    final var abertaEm = Instant.parse("2026-01-19T12:00:00Z");
    final var fechaEm = abertaEm.plusSeconds(60);

    final var sessaoAberta =
        new SessaoVotacao(UUID.randomUUID(), pautaId, abertaEm, fechaEm, SessaoStatus.ABERTA);

    assertTrue(sessaoAberta.estaAbertaEm(abertaEm.plusSeconds(10)));
    assertFalse(sessaoAberta.estaAbertaEm(fechaEm)); // isBefore → false quando igual

    final var sessaoEncerrada =
        new SessaoVotacao(UUID.randomUUID(), pautaId, abertaEm, fechaEm, SessaoStatus.ENCERRADA);

    assertFalse(sessaoEncerrada.estaAbertaEm(abertaEm.plusSeconds(10)));
  }

  @Test
  void encerrarDeveRetornarMesmaInstanciaQuandoJaEncerradaESenaoCriarNovaComStatusEncerrada() {
    final var pautaId = UUID.randomUUID();
    final var abertaEm = Instant.parse("2026-01-19T12:00:00Z");
    final var fechaEm = abertaEm.plusSeconds(60);

    final var aberta =
        new SessaoVotacao(UUID.randomUUID(), pautaId, abertaEm, fechaEm, SessaoStatus.ABERTA);

    final var encerrada = aberta.encerrar();
    assertNotSame(aberta, encerrada);
    assertEquals(SessaoStatus.ENCERRADA, encerrada.getStatus());

    final var jaEncerrada =
        new SessaoVotacao(UUID.randomUUID(), pautaId, abertaEm, fechaEm, SessaoStatus.ENCERRADA);

    assertSame(jaEncerrada, jaEncerrada.encerrar());
  }
}
