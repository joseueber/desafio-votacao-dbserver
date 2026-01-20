package com.dbserver.votacao.infrastructure.web.controller;

import com.dbserver.votacao.application.usecase.AbrirSessaoUseCase;
import com.dbserver.votacao.application.usecase.commands.AbrirSessaoCommand;
import com.dbserver.votacao.infrastructure.web.dto.AbrirSessaoRequest;
import com.dbserver.votacao.infrastructure.web.dto.AbrirSessaoResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/pautas/{pautaId}/sessao")
public class SessaoController {

  private final AbrirSessaoUseCase abrirSessaoUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AbrirSessaoResponse abrir(
      @PathVariable UUID pautaId,
      @Valid @RequestBody(required = false) AbrirSessaoRequest request) {

    log.info("Recebida requisição para abrir sessão para a pauta: {}", pautaId);

    final var duracao =
        (request == null || request.duracaoEmMinutos() == null)
            ? null
            : Duration.ofMinutes(request.duracaoEmMinutos());

    var sessao = abrirSessaoUseCase.executar(new AbrirSessaoCommand(pautaId, duracao));

    log.info("Sessão aberta com sucesso para a pauta: {}. Expira em: {}", pautaId, sessao.getFechaEm());

    return new AbrirSessaoResponse(
        sessao.getId(),
        sessao.getPautaId(),
        sessao.getAbertaEm(),
        sessao.getFechaEm(),
        sessao.getStatus().name());
  }
}
