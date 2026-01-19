package com.dbserver.votacao.infrastructure.web.controller;

import com.dbserver.votacao.application.usecase.ConsultarResultadoUseCase;
import com.dbserver.votacao.infrastructure.web.dto.ResultadoVotacaoResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pautas/{pautaId}/resultado")
public class ResultadoController {

  private final ConsultarResultadoUseCase consultarResultadoUseCase;

  @GetMapping
  public ResultadoVotacaoResponse consultar(@PathVariable UUID pautaId) {
    var result = consultarResultadoUseCase.executar(pautaId);
    return new ResultadoVotacaoResponse(
        result.pautaId(), result.totalSim(), result.totalNao(), result.total());
  }
}
