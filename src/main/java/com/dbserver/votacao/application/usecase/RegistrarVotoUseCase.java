package com.dbserver.votacao.application.usecase;

import com.dbserver.votacao.application.exceptions.RecursoNaoEncontradoException;
import com.dbserver.votacao.application.exceptions.RegraDeNegocioException;
import com.dbserver.votacao.application.ports.ClockPort;
import com.dbserver.votacao.application.ports.CpfValidationPort;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.ports.SessaoRepositoryPort;
import com.dbserver.votacao.application.ports.VotoRepositoryPort;
import com.dbserver.votacao.application.usecase.commands.RegistrarVotoCommand;
import com.dbserver.votacao.domain.model.Voto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RegistrarVotoUseCase {

  private final PautaRepositoryPort pautaRepository;
  private final SessaoRepositoryPort sessaoRepository;
  private final VotoRepositoryPort votoRepository;
  private final CpfValidationPort cpfValidation;
  private final ClockPort clock;

  public Voto executar(RegistrarVotoCommand command) {
    final var pautaId = command.pautaId();
    final var cpf = command.associadoCpf();

    log.debug("Iniciando registro de voto. Pauta: {}, CPF: {}", pautaId, cpf);

    pautaRepository
        .buscarPorId(pautaId)
        .orElseThrow(
            () -> {
              log.warn("Falha ao votar: Pauta {} não encontrada", pautaId);
              return new RecursoNaoEncontradoException("Pauta não encontrada");
            });

    final var sessao =
        sessaoRepository
            .buscarPorPautaId(pautaId)
            .orElseThrow(
                () -> {
                  log.warn("Falha ao votar: Sessão não encontrada para pauta {}", pautaId);
                  return new RegraDeNegocioException("Sessão não encontrada para esta pauta");
                });

    if (!sessao.estaAbertaEm(clock.agora())) {
      log.warn("Falha ao votar: Sessão encerrada para pauta {}", pautaId);
      throw new RegraDeNegocioException("Sessão de votação encerrada");
    }

    final var result = cpfValidation.validar(cpf);
    if (result == CpfValidationPort.CpfValidationResult.UNABLE_TO_VOTE) {
      log.warn("Falha ao votar: CPF {} não habilitado para votar na pauta {}", cpf, pautaId);
      throw new RegraDeNegocioException("CPF não habilitado para votar");
    }

    if (votoRepository.existeVotoDaPautaPorCpf(pautaId, cpf)) {
      log.warn("Falha ao votar: CPF {} já votou na pauta {}", cpf, pautaId);
      throw new RegraDeNegocioException("CPF já votou nesta pauta");
    }

    final var voto = Voto.novo(pautaId, cpf, command.valor(), clock.agora());
    var votoSalvo = votoRepository.salvar(voto);
    log.info("Voto {} registrado com sucesso para a pauta {}", votoSalvo.getId(), pautaId);
    return votoSalvo;
  }
}
