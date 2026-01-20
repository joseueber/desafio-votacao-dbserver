package com.dbserver.votacao.application.usecase;

import com.dbserver.votacao.application.exceptions.RecursoNaoEncontradoException;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.ports.VotoRepositoryPort;
import com.dbserver.votacao.application.usecase.results.ResultadoVotacaoResult;
import com.dbserver.votacao.domain.enums.VotoValor;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ConsultarResultadoUseCase {

  private final PautaRepositoryPort pautaRepository;
  private final VotoRepositoryPort votoRepository;

  public ResultadoVotacaoResult executar(UUID pautaId) {
    log.debug("Iniciando consulta de resultado para a pauta: {}", pautaId);
    pautaRepository
        .buscarPorId(pautaId)
        .orElseThrow(() -> {
          log.warn("Falha ao consultar resultado: Pauta {} não encontrada", pautaId);
          return new RecursoNaoEncontradoException("Pauta não encontrada");
        });

    final var contagem = votoRepository.contarVotosPorPauta(pautaId);
    final var sim = contagem.getOrDefault(VotoValor.SIM, 0L);
    final var nao = contagem.getOrDefault(VotoValor.NAO, 0L);

    log.info("Resultado apurado para a pauta {}: SIM={}, NAO={}, Total={}", pautaId, sim, nao, sim + nao);
    return new ResultadoVotacaoResult(pautaId, sim, nao, sim + nao);
  }
}
