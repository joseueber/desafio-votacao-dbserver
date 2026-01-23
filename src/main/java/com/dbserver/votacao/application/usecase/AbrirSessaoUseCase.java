package com.dbserver.votacao.application.usecase;

import com.dbserver.votacao.application.exceptions.RecursoNaoEncontradoException;
import com.dbserver.votacao.application.exceptions.RegraDeNegocioException;
import com.dbserver.votacao.application.ports.ClockPort;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.ports.SessaoRepositoryPort;
import com.dbserver.votacao.application.usecase.commands.AbrirSessaoCommand;
import com.dbserver.votacao.domain.model.SessaoVotacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AbrirSessaoUseCase {

  private final PautaRepositoryPort pautaRepository;
  private final SessaoRepositoryPort sessaoRepository;
  private final ClockPort clock;

  public SessaoVotacao executar(AbrirSessaoCommand command) {
    final var pautaId = command.pautaId();
    log.debug("Iniciando abertura de sessão para a pauta: {}", pautaId);

    pautaRepository
        .buscarPorId(pautaId)
        .orElseThrow(
            () -> {
              log.warn("Falha ao abrir sessão: Pauta {} não encontrada", pautaId);
              return new RecursoNaoEncontradoException("Pauta não encontrada");
            });

    sessaoRepository
        .buscarPorPautaId(pautaId)
        .ifPresent(
            s -> {
              log.warn("Falha ao abrir sessão: Pauta {} já possui sessão aberta", pautaId);
              throw new RegraDeNegocioException("Já existe sessão para esta pauta");
            });

    final var sessao = SessaoVotacao.abrir(pautaId, clock.agora(), command.duracao());
    var sessaoSalva = sessaoRepository.salvar(sessao);
    log.info(
        "Sessão {} aberta com sucesso para a pauta {}. Expira em {}",
        sessaoSalva.getId(),
        pautaId,
        sessaoSalva.getFechaEm());
    return sessaoSalva;
  }
}
