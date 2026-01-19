package com.dbserver.votacao.application.ports;

import com.dbserver.votacao.domain.Pauta;
import java.util.Optional;
import java.util.UUID;

public interface PautaRepositoryPort {
  Pauta salvar(Pauta pauta);

  Optional<Pauta> buscarPorId(UUID id);
}
