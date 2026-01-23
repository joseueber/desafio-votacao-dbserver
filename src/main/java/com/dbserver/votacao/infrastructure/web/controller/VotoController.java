package com.dbserver.votacao.infrastructure.web.controller;

import com.dbserver.votacao.application.usecase.RegistrarVotoUseCase;
import com.dbserver.votacao.application.usecase.commands.RegistrarVotoCommand;
import com.dbserver.votacao.domain.enums.VotoValor;
import com.dbserver.votacao.infrastructure.web.dto.RegistrarVotoRequest;
import com.dbserver.votacao.infrastructure.web.dto.RegistrarVotoResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/pautas/{pautaId}/votos")
public class VotoController {

  private final RegistrarVotoUseCase registrarVotoUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RegistrarVotoResponse votar(
      @PathVariable UUID pautaId, @Valid @RequestBody RegistrarVotoRequest request) {
    log.info(
        "Recebida requisição de voto para a pauta: {}, associado: {}",
        pautaId,
        request.associadoCpf());
    var voto =
        registrarVotoUseCase.executar(
            new RegistrarVotoCommand(
                pautaId, request.associadoCpf(), VotoValor.valueOf(request.valor())));
    log.info("Voto registrado com sucesso para a pauta: {}, id do voto: {}", pautaId, voto.getId());
    return new RegistrarVotoResponse(voto.getId());
  }
}
