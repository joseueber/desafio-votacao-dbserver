package com.dbserver.votacao.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dbserver.votacao.application.exceptions.RecursoNaoEncontradoException;
import com.dbserver.votacao.application.exceptions.RegraDeNegocioException;
import com.dbserver.votacao.application.ports.ClockPort;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.ports.SessaoRepositoryPort;
import com.dbserver.votacao.application.usecase.commands.AbrirSessaoCommand;
import com.dbserver.votacao.domain.model.Pauta;
import com.dbserver.votacao.domain.model.SessaoVotacao;
import com.dbserver.votacao.domain.enums.SessaoStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AbrirSessaoUseCaseTest {

  @Test
  void deveAbrirSessaoQuandoPautaExisteENaoHaSessao() {
    final var pautaRepo = mock(PautaRepositoryPort.class);
    final var sessaoRepo = mock(SessaoRepositoryPort.class);
    final var clock = mock(ClockPort.class);
    final var pautaId = UUID.randomUUID();
    final var agora = Instant.parse("2026-01-19T12:00:00Z");

    when(clock.agora()).thenReturn(agora);
    when(pautaRepo.buscarPorId(pautaId))
        .thenReturn(Optional.of(new Pauta(UUID.randomUUID(), "Pauta", null, agora)));
    when(sessaoRepo.buscarPorPautaId(pautaId)).thenReturn(Optional.empty());
    when(sessaoRepo.salvar(any(SessaoVotacao.class)))
        .thenAnswer(
            inv -> {
              SessaoVotacao s = inv.getArgument(0);
              return new SessaoVotacao(
                  UUID.randomUUID(),
                  s.getPautaId(),
                  s.getAbertaEm(),
                  s.getFechaEm(),
                  s.getStatus());
            });

    final var useCase = new AbrirSessaoUseCase(pautaRepo, sessaoRepo, clock);
    final var result = useCase.executar(new AbrirSessaoCommand(pautaId, Duration.ofMinutes(2)));

    assertNotNull(result.getId());
    assertEquals(pautaId, result.getPautaId());
    assertEquals(agora, result.getAbertaEm());
    assertEquals(agora.plus(Duration.ofMinutes(2)), result.getFechaEm());
    assertEquals(SessaoStatus.ABERTA, result.getStatus());
  }

  @Test
  void deveFalharQuandoPautaNaoExiste() {
    final var pautaRepo = mock(PautaRepositoryPort.class);
    final var sessaoRepo = mock(SessaoRepositoryPort.class);
    final var clock = mock(ClockPort.class);
    final var pautaId = UUID.randomUUID();

    when(pautaRepo.buscarPorId(pautaId)).thenReturn(Optional.empty());

    final var useCase = new AbrirSessaoUseCase(pautaRepo, sessaoRepo, clock);

    assertThrows(
        RecursoNaoEncontradoException.class,
        () -> useCase.executar(new AbrirSessaoCommand(pautaId, Duration.ofMinutes(1))));
  }

  @Test
  void deveFalharQuandoJaExisteSessaoParaPauta() {
    final var pautaRepo = mock(PautaRepositoryPort.class);
    final var sessaoRepo = mock(SessaoRepositoryPort.class);
    final var clock = mock(ClockPort.class);
    final var pautaId = UUID.randomUUID();
    final var agora = Instant.parse("2026-01-19T12:00:00Z");

    when(pautaRepo.buscarPorId(pautaId))
        .thenReturn(Optional.of(new Pauta(UUID.randomUUID(), "Pauta", null, agora)));
    when(sessaoRepo.buscarPorPautaId(pautaId))
        .thenReturn(
            Optional.of(
                new SessaoVotacao(
                    UUID.randomUUID(),
                    pautaId,
                    agora,
                    agora.plus(Duration.ofMinutes(1)),
                    SessaoStatus.ABERTA)));

    final var useCase = new AbrirSessaoUseCase(pautaRepo, sessaoRepo, clock);

    assertThrows(
        RegraDeNegocioException.class,
        () -> useCase.executar(new AbrirSessaoCommand(pautaId, Duration.ofMinutes(1))));
  }
}
