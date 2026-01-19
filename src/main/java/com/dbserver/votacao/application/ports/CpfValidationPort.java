package com.dbserver.votacao.application.ports;

public interface CpfValidationPort {

  CpfValidationResult validar(String cpf);

  enum CpfValidationResult {
    ABLE_TO_VOTE,
    UNABLE_TO_VOTE
  }
}
