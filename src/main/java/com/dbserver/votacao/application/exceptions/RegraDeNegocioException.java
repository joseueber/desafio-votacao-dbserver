package com.dbserver.votacao.application.exceptions;

public class RegraDeNegocioException extends RuntimeException {
  public RegraDeNegocioException(String message) {
    super(message);
  }
}
