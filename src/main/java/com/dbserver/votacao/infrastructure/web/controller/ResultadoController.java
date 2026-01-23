package com.dbserver.votacao.infrastructure.web.controller;

import com.dbserver.votacao.application.usecase.ConsultarResultadoUseCase;
import com.dbserver.votacao.infrastructure.web.dto.ResultadoVotacaoResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/pautas/{pautaId}/resultado")
public class ResultadoController {

  private final ConsultarResultadoUseCase consultarResultadoUseCase;

  @GetMapping
  public ResultadoVotacaoResponse consultar(@PathVariable UUID pautaId) {
    log.info("Recebida requisição para consultar resultado da pauta: {}", pautaId);
    var result = consultarResultadoUseCase.executar(pautaId);
    log.info(
        "Resultado consultado com sucesso para a pauta: {}. Total: {}", pautaId, result.total());
    return new ResultadoVotacaoResponse(
        result.pautaId(), result.totalSim(), result.totalNao(), result.total());
  }
}
