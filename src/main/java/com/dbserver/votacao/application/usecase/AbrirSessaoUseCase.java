package com.dbserver.votacao.application.usecase;

import com.dbserver.votacao.application.exceptions.RecursoNaoEncontradoException;
import com.dbserver.votacao.application.exceptions.RegraDeNegocioException;
import com.dbserver.votacao.application.ports.ClockPort;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.ports.SessaoRepositoryPort;
import com.dbserver.votacao.application.usecase.commands.AbrirSessaoCommand;
import com.dbserver.votacao.domain.SessaoVotacao;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbrirSessaoUseCase {

  private final PautaRepositoryPort pautaRepository;
  private final SessaoRepositoryPort sessaoRepository;
  private final ClockPort clock;

  public SessaoVotacao executar(AbrirSessaoCommand command) {
    final var pautaId = command.pautaId();

    pautaRepository
        .buscarPorId(pautaId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada"));

    sessaoRepository
        .buscarPorPautaId(pautaId)
        .ifPresent(
            s -> {
              throw new RegraDeNegocioException("Já existe sessão para esta pauta");
            });

    final var sessao =
        SessaoVotacao.abrir(UUID.randomUUID(), pautaId, clock.agora(), command.duracao());
    return sessaoRepository.salvar(sessao);
  }
}
