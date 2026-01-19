package com.dbserver.votacao.infrastructure.persistence.adapter;

import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.domain.Pauta;
import com.dbserver.votacao.infrastructure.persistence.mapper.PautaMapper;
import com.dbserver.votacao.infrastructure.persistence.repository.PautaJpaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PautaPersistenceAdapter implements PautaRepositoryPort {

  private final PautaJpaRepository repository;

  @Override
  public Pauta salvar(Pauta pauta) {
    var saved = repository.save(PautaMapper.toEntity(pauta));
    return PautaMapper.toDomain(saved);
  }

  @Override
  public Optional<Pauta> buscarPorId(UUID id) {
    return repository.findById(id).map(PautaMapper::toDomain);
  }
}
