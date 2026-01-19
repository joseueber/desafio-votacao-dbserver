package com.dbserver.votacao.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.dbserver.votacao.application.ports.ClockPort;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.usecase.commands.CadastrarPautaCommand;
import com.dbserver.votacao.domain.Pauta;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class CadastrarPautaUseCaseTest {

  @Test
  void deveCadastrarPautaComDadosValidos() {
    final var pautaRepository = mock(PautaRepositoryPort.class);
    final var clock = mock(ClockPort.class);
    final var agora = Instant.parse("2026-01-19T12:00:00Z");

    when(clock.agora()).thenReturn(agora);
    when(pautaRepository.salvar(any(Pauta.class)))
        .thenAnswer(
            inv -> {
              Pauta p = inv.getArgument(0);
              return new Pauta(
                  java.util.UUID.randomUUID(), p.getTitulo(), p.getDescricao(), p.getCriadaEm());
            });

    final var useCase = new CadastrarPautaUseCase(pautaRepository, clock);
    final var result = useCase.executar(new CadastrarPautaCommand("Pauta 1", "Descrição"));

    assertNotNull(result.getId());
    assertEquals("Pauta 1", result.getTitulo());
    assertEquals("Descrição", result.getDescricao());
    assertEquals(agora, result.getCriadaEm());

    verify(pautaRepository, times(1)).salvar(any(Pauta.class));
  }
}
