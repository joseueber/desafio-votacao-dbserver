package com.dbserver.votacao.infrastructure.persistence.adapter;

import com.dbserver.votacao.application.ports.VotoRepositoryPort;
import com.dbserver.votacao.domain.enums.VotoValor;
import com.dbserver.votacao.domain.model.Voto;
import com.dbserver.votacao.infrastructure.persistence.mapper.VotoMapper;
import com.dbserver.votacao.infrastructure.persistence.repository.VotoJpaRepository;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VotoPersistenceAdapter implements VotoRepositoryPort {

  private final VotoJpaRepository repository;

  @Override
  public Voto salvar(Voto voto) {
    log.debug(
        "Persistindo voto para a pauta: {}, associado: {}",
        voto.getPautaId(),
        voto.getAssociadoCpf());
    var saved = repository.save(VotoMapper.toEntity(voto));
    return VotoMapper.toDomain(saved);
  }

  @Override
  public boolean existeVotoDaPautaPorCpf(UUID pautaId, String associadoCpf) {
    return repository.existsByPautaIdAndAssociadoCpf(pautaId, associadoCpf);
  }

  @Override
  public Map<VotoValor, Long> contarVotosPorPauta(UUID pautaId) {
    Map<VotoValor, Long> result = new EnumMap<>(VotoValor.class);
    for (Object[] row : repository.contarPorValor(pautaId)) {
      result.put(VotoValor.valueOf((String) row[0]), (Long) row[1]);
    }
    return result;
  }
}
