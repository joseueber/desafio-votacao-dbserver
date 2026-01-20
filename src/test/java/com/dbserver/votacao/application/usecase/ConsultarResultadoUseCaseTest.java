package com.dbserver.votacao.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dbserver.votacao.application.exceptions.RecursoNaoEncontradoException;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.ports.VotoRepositoryPort;
import com.dbserver.votacao.domain.model.Pauta;
import com.dbserver.votacao.domain.enums.VotoValor;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ConsultarResultadoUseCaseTest {

  @Test
  void deveRetornarResultadoQuandoPautaExiste() {
    final var pautaRepo = mock(PautaRepositoryPort.class);
    final var votoRepo = mock(VotoRepositoryPort.class);

    final var pautaId = UUID.randomUUID();
    final var agora = Instant.parse("2026-01-19T12:00:00Z");

    when(pautaRepo.buscarPorId(pautaId))
        .thenReturn(Optional.of(new Pauta(UUID.randomUUID(), "Pauta", null, agora)));

    Map<VotoValor, Long> contagem = new EnumMap<>(VotoValor.class);
    contagem.put(VotoValor.SIM, 3L);
    contagem.put(VotoValor.NAO, 1L);

    when(votoRepo.contarVotosPorPauta(pautaId)).thenReturn(contagem);

    final var useCase = new ConsultarResultadoUseCase(pautaRepo, votoRepo);

    final var result = useCase.executar(pautaId);

    assertEquals(pautaId, result.pautaId());
    assertEquals(3L, result.totalSim());
    assertEquals(1L, result.totalNao());
    assertEquals(4L, result.total());
  }

  @Test
  void deveFalharQuandoPautaNaoExiste() {
    final var pautaRepo = mock(PautaRepositoryPort.class);
    final var votoRepo = mock(VotoRepositoryPort.class);

    final var pautaId = UUID.randomUUID();
    when(pautaRepo.buscarPorId(pautaId)).thenReturn(Optional.empty());

    final var useCase = new ConsultarResultadoUseCase(pautaRepo, votoRepo);

    assertThrows(RecursoNaoEncontradoException.class, () -> useCase.executar(pautaId));
  }
}
