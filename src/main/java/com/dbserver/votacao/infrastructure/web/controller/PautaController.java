package com.dbserver.votacao.infrastructure.web.controller;

import com.dbserver.votacao.application.usecase.CadastrarPautaUseCase;
import com.dbserver.votacao.application.usecase.commands.CadastrarPautaCommand;
import com.dbserver.votacao.infrastructure.web.dto.CriarPautaRequest;
import com.dbserver.votacao.infrastructure.web.dto.CriarPautaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pautas")
public class PautaController {

  private final CadastrarPautaUseCase cadastrarPautaUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CriarPautaResponse criar(@Valid @RequestBody CriarPautaRequest request) {
    var pauta =
        cadastrarPautaUseCase.executar(
            new CadastrarPautaCommand(request.titulo(), request.descricao()));
    return new CriarPautaResponse(pauta.getId());
  }
}
