package com.dbserver.votacao.infrastructure.persistence.adapter;

import com.dbserver.votacao.application.ports.SessaoRepositoryPort;
import com.dbserver.votacao.domain.SessaoVotacao;
import com.dbserver.votacao.infrastructure.persistence.mapper.SessaoVotacaoMapper;
import com.dbserver.votacao.infrastructure.persistence.repository.SessaoVotacaoJpaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessaoPersistenceAdapter implements SessaoRepositoryPort {

  private final SessaoVotacaoJpaRepository repository;

  @Override
  public SessaoVotacao salvar(SessaoVotacao sessao) {
    var saved = repository.save(SessaoVotacaoMapper.toEntity(sessao));
    return SessaoVotacaoMapper.toDomain(saved);
  }

  @Override
  public Optional<SessaoVotacao> buscarPorPautaId(UUID pautaId) {
    return repository.findByPautaId(pautaId).map(SessaoVotacaoMapper::toDomain);
  }
}
