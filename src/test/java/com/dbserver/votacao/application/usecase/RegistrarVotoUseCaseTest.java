package com.dbserver.votacao.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dbserver.votacao.application.exceptions.RecursoNaoEncontradoException;
import com.dbserver.votacao.application.exceptions.RegraDeNegocioException;
import com.dbserver.votacao.application.ports.ClockPort;
import com.dbserver.votacao.application.ports.CpfValidationPort;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.ports.SessaoRepositoryPort;
import com.dbserver.votacao.application.ports.VotoRepositoryPort;
import com.dbserver.votacao.application.usecase.commands.RegistrarVotoCommand;
import com.dbserver.votacao.domain.Pauta;
import com.dbserver.votacao.domain.SessaoVotacao;
import com.dbserver.votacao.domain.Voto;
import com.dbserver.votacao.domain.enums.SessaoStatus;
import com.dbserver.votacao.domain.enums.VotoValor;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RegistrarVotoUseCaseTest {

  @Test
  void deveRegistrarVotoQuandoSessaoAbertaECpfValidoESemVotoAnterior() {
    final var pautaRepo = mock(PautaRepositoryPort.class);
    final var sessaoRepo = mock(SessaoRepositoryPort.class);
    final var votoRepo = mock(VotoRepositoryPort.class);
    final var cpfPort = mock(CpfValidationPort.class);
    final var clock = mock(ClockPort.class);
    final var pautaId = UUID.randomUUID();
    final var agora = Instant.parse("2026-01-19T12:00:00Z");

    when(clock.agora()).thenReturn(agora);
    when(pautaRepo.buscarPorId(pautaId))
        .thenReturn(Optional.of(new Pauta(UUID.randomUUID(), "Pauta", null, agora)));
    when(sessaoRepo.buscarPorPautaId(pautaId))
        .thenReturn(
            Optional.of(
                new SessaoVotacao(
                    UUID.randomUUID(),
                    pautaId,
                    agora.minusSeconds(10),
                    agora.plusSeconds(60),
                    SessaoStatus.ABERTA)));
    when(cpfPort.validar("12345678901"))
        .thenReturn(CpfValidationPort.CpfValidationResult.ABLE_TO_VOTE);
    when(votoRepo.existeVotoDaPautaPorCpf(pautaId, "12345678901")).thenReturn(false);
    when(votoRepo.salvar(any(Voto.class)))
        .thenAnswer(
            inv -> {
              Voto v = inv.getArgument(0);
              return new Voto(
                  UUID.randomUUID(),
                  v.getPautaId(),
                  v.getAssociadoCpf(),
                  v.getValor(),
                  v.getCriadoEm());
            });

    final var useCase = new RegistrarVotoUseCase(pautaRepo, sessaoRepo, votoRepo, cpfPort, clock);
    final var result =
        useCase.executar(new RegistrarVotoCommand(pautaId, "12345678901", VotoValor.SIM));

    assertNotNull(result.getId());
    assertEquals(pautaId, result.getPautaId());
    assertEquals("12345678901", result.getAssociadoCpf());
    assertEquals(VotoValor.SIM, result.getValor());
    assertEquals(agora, result.getCriadoEm());
  }

  @Test
  void deveFalharQuandoSessaoEncerrada() {
    final var pautaRepo = mock(PautaRepositoryPort.class);
    final var sessaoRepo = mock(SessaoRepositoryPort.class);
    final var votoRepo = mock(VotoRepositoryPort.class);
    final var cpfPort = mock(CpfValidationPort.class);
    final var clock = mock(ClockPort.class);
    final var pautaId = UUID.randomUUID();
    final var agora = Instant.parse("2026-01-19T12:00:00Z");

    when(clock.agora()).thenReturn(agora);
    when(pautaRepo.buscarPorId(pautaId))
        .thenReturn(Optional.of(new Pauta(UUID.randomUUID(), "Pauta", null, agora)));
    when(sessaoRepo.buscarPorPautaId(pautaId))
        .thenReturn(
            Optional.of(
                new SessaoVotacao(
                    UUID.randomUUID(),
                    pautaId,
                    agora.minusSeconds(120),
                    agora.minusSeconds(60),
                    SessaoStatus.ABERTA)));

    final var useCase = new RegistrarVotoUseCase(pautaRepo, sessaoRepo, votoRepo, cpfPort, clock);

    assertThrows(
        RegraDeNegocioException.class,
        () -> useCase.executar(new RegistrarVotoCommand(pautaId, "12345678901", VotoValor.SIM)));
  }

  @Test
  void deveFalharQuandoCpfInvalidoNoServico() {
    final var pautaRepo = mock(PautaRepositoryPort.class);
    final var sessaoRepo = mock(SessaoRepositoryPort.class);
    final var votoRepo = mock(VotoRepositoryPort.class);
    final var cpfPort = mock(CpfValidationPort.class);
    final var clock = mock(ClockPort.class);
    final var pautaId = UUID.randomUUID();
    final var agora = Instant.parse("2026-01-19T12:00:00Z");

    when(clock.agora()).thenReturn(agora);
    when(pautaRepo.buscarPorId(pautaId))
        .thenReturn(Optional.of(new Pauta(UUID.randomUUID(), "Pauta", null, agora)));
    when(sessaoRepo.buscarPorPautaId(pautaId))
        .thenReturn(
            Optional.of(
                new SessaoVotacao(
                    UUID.randomUUID(),
                    pautaId,
                    agora.minusSeconds(10),
                    agora.plusSeconds(60),
                    SessaoStatus.ABERTA)));
    when(cpfPort.validar("12345678901"))
        .thenReturn(CpfValidationPort.CpfValidationResult.UNABLE_TO_VOTE);

    final var useCase = new RegistrarVotoUseCase(pautaRepo, sessaoRepo, votoRepo, cpfPort, clock);

    assertThrows(
        RecursoNaoEncontradoException.class,
        () -> useCase.executar(new RegistrarVotoCommand(pautaId, "12345678901", VotoValor.SIM)));
  }

  @Test
  void deveFalharQuandoCpfJaVotou() {
    final var pautaRepo = mock(PautaRepositoryPort.class);
    final var sessaoRepo = mock(SessaoRepositoryPort.class);
    final var votoRepo = mock(VotoRepositoryPort.class);
    final var cpfPort = mock(CpfValidationPort.class);
    final var clock = mock(ClockPort.class);
    final var pautaId = UUID.randomUUID();
    final var agora = Instant.parse("2026-01-19T12:00:00Z");

    when(clock.agora()).thenReturn(agora);
    when(pautaRepo.buscarPorId(pautaId))
        .thenReturn(Optional.of(new Pauta(UUID.randomUUID(), "Pauta", null, agora)));
    when(sessaoRepo.buscarPorPautaId(pautaId))
        .thenReturn(
            Optional.of(
                new SessaoVotacao(
                    UUID.randomUUID(),
                    pautaId,
                    agora.minusSeconds(10),
                    agora.plusSeconds(60),
                    SessaoStatus.ABERTA)));
    when(cpfPort.validar("12345678901"))
        .thenReturn(CpfValidationPort.CpfValidationResult.ABLE_TO_VOTE);
    when(votoRepo.existeVotoDaPautaPorCpf(pautaId, "12345678901")).thenReturn(true);

    final var useCase = new RegistrarVotoUseCase(pautaRepo, sessaoRepo, votoRepo, cpfPort, clock);

    assertThrows(
        RegraDeNegocioException.class,
        () -> useCase.executar(new RegistrarVotoCommand(pautaId, "12345678901", VotoValor.SIM)));
  }

  @Test
  void deveFalharQuandoNaoExisteSessaoParaPauta() {
    final var pautaRepo = mock(PautaRepositoryPort.class);
    final var sessaoRepo = mock(SessaoRepositoryPort.class);
    final var votoRepo = mock(VotoRepositoryPort.class);
    final var cpfPort = mock(CpfValidationPort.class);
    final var clock = mock(ClockPort.class);
    final var pautaId = UUID.randomUUID();
    final var agora = Instant.parse("2026-01-19T12:00:00Z");

    when(clock.agora()).thenReturn(agora);
    when(pautaRepo.buscarPorId(pautaId))
            .thenReturn(Optional.of(new Pauta(UUID.randomUUID(), "Pauta", null, agora)));
    when(sessaoRepo.buscarPorPautaId(pautaId)).thenReturn(Optional.empty());

    final var useCase = new RegistrarVotoUseCase(pautaRepo, sessaoRepo, votoRepo, cpfPort, clock);
    final var ex =
            assertThrows(
                    RegraDeNegocioException.class,
                    () -> useCase.executar(new RegistrarVotoCommand(pautaId, "12345678901", VotoValor.SIM)));

    assertEquals("Sessão não encontrada para esta pauta", ex.getMessage());
    verifyNoInteractions(votoRepo, cpfPort);
  }
}
