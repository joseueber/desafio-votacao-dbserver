package com.dbserver.votacao.application.ports;

import com.dbserver.votacao.domain.model.Voto;
import com.dbserver.votacao.domain.enums.VotoValor;
import java.util.Map;
import java.util.UUID;

public interface VotoRepositoryPort {
  Voto salvar(Voto voto);

  boolean existeVotoDaPautaPorCpf(UUID pautaId, String associadoCpf);

  Map<VotoValor, Long> contarVotosPorPauta(UUID pautaId);
}
