package com.dbserver.votacao.application.usecase;

import com.dbserver.votacao.application.exceptions.RecursoNaoEncontradoException;
import com.dbserver.votacao.application.exceptions.RegraDeNegocioException;
import com.dbserver.votacao.application.ports.ClockPort;
import com.dbserver.votacao.application.ports.CpfValidationPort;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.ports.SessaoRepositoryPort;
import com.dbserver.votacao.application.ports.VotoRepositoryPort;
import com.dbserver.votacao.application.usecase.commands.RegistrarVotoCommand;
import com.dbserver.votacao.domain.Voto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegistrarVotoUseCase {

  private final PautaRepositoryPort pautaRepository;
  private final SessaoRepositoryPort sessaoRepository;
  private final VotoRepositoryPort votoRepository;
  private final CpfValidationPort cpfValidation;
  private final ClockPort clock;

  public Voto executar(RegistrarVotoCommand command) {
    final var pautaId = command.pautaId();

    pautaRepository
        .buscarPorId(pautaId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada"));

    final var sessao =
        sessaoRepository
            .buscarPorPautaId(pautaId)
            .orElseThrow(
                () -> new RegraDeNegocioException("Sessão não encontrada para esta pauta"));

    if (!sessao.estaAbertaEm(clock.agora())) {
      throw new RegraDeNegocioException("Sessão de votação encerrada");
    }

    final var cpfNormalizado = command.associadoCpf();
    final var result = cpfValidation.validar(cpfNormalizado);
    if (result == CpfValidationPort.CpfValidationResult.UNABLE_TO_VOTE) {
      throw new RegraDeNegocioException("CPF não habilitado para votar");
    }

    if (votoRepository.existeVotoDaPautaPorCpf(pautaId, cpfNormalizado)) {
      throw new RegraDeNegocioException("CPF já votou nesta pauta");
    }

    final var voto = Voto.novo(pautaId, cpfNormalizado, command.valor(), clock.agora());
    return votoRepository.salvar(voto);
  }
}
