package com.dbserver.votacao.application.ports;

import com.dbserver.votacao.domain.SessaoVotacao;
import java.util.Optional;
import java.util.UUID;

public interface SessaoRepositoryPort {
  SessaoVotacao salvar(SessaoVotacao sessao);

  Optional<SessaoVotacao> buscarPorPautaId(UUID pautaId);
}
