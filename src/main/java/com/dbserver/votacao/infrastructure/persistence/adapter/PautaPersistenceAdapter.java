package com.dbserver.votacao.infrastructure.persistence.adapter;

import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.domain.model.Pauta;
import com.dbserver.votacao.infrastructure.persistence.mapper.PautaMapper;
import com.dbserver.votacao.infrastructure.persistence.repository.PautaJpaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PautaPersistenceAdapter implements PautaRepositoryPort {

  private final PautaJpaRepository repository;

  @Override
  public Pauta salvar(Pauta pauta) {
    log.debug("Persistindo pauta: {}", pauta.getTitulo());
    var saved = repository.save(PautaMapper.toEntity(pauta));
    return PautaMapper.toDomain(saved);
  }

  @Override
  public Optional<Pauta> buscarPorId(UUID id) {
    return repository.findById(id).map(PautaMapper::toDomain);
  }
}
