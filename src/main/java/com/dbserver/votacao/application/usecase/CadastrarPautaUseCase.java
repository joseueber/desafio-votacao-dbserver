package com.dbserver.votacao.application.usecase;

import com.dbserver.votacao.application.ports.ClockPort;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.usecase.commands.CadastrarPautaCommand;
import com.dbserver.votacao.domain.Pauta;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CadastrarPautaUseCase {

  private final PautaRepositoryPort pautaRepository;
  private final ClockPort clock;

  public Pauta executar(CadastrarPautaCommand command) {
    final var pauta = Pauta.nova(command.titulo(), command.descricao(), clock.agora());
    return pautaRepository.salvar(pauta);
  }
}
