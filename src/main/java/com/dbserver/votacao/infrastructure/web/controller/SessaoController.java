package com.dbserver.votacao.infrastructure.web.controller;

import com.dbserver.votacao.application.usecase.AbrirSessaoUseCase;
import com.dbserver.votacao.application.usecase.commands.AbrirSessaoCommand;
import com.dbserver.votacao.infrastructure.web.dto.AbrirSessaoRequest;
import com.dbserver.votacao.infrastructure.web.dto.AbrirSessaoResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pautas/{pautaId}/sessao")
public class SessaoController {

  private final AbrirSessaoUseCase abrirSessaoUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AbrirSessaoResponse abrir(
      @PathVariable UUID pautaId,
      @Valid @RequestBody(required = false) AbrirSessaoRequest request) {

    final var duracao =
        (request == null || request.duracaoEmMinutos() == null)
            ? null
            : Duration.ofMinutes(request.duracaoEmMinutos());

    var sessao = abrirSessaoUseCase.executar(new AbrirSessaoCommand(pautaId, duracao));
    return new AbrirSessaoResponse(
        sessao.getId(),
        sessao.getPautaId(),
        sessao.getAbertaEm(),
        sessao.getFechaEm(),
        sessao.getStatus().name());
  }
}
