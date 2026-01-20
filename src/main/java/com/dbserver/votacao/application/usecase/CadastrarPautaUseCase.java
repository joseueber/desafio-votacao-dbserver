package com.dbserver.votacao.application.usecase;

import com.dbserver.votacao.application.ports.ClockPort;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.usecase.commands.CadastrarPautaCommand;
import com.dbserver.votacao.domain.model.Pauta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CadastrarPautaUseCase {

  private final PautaRepositoryPort pautaRepository;
  private final ClockPort clock;

  public Pauta executar(CadastrarPautaCommand command) {
    log.debug("Iniciando cadastro de pauta: {}", command.titulo());
    final var pauta = Pauta.nova(command.titulo(), command.descricao(), clock.agora());
    var pautaSalva = pautaRepository.salvar(pauta);
    log.info("Pauta {} cadastrada com sucesso: {}", pautaSalva.getId(), pautaSalva.getTitulo());
    return pautaSalva;
  }
}
