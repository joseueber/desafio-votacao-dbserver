package com.dbserver.votacao.infrastructure.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.dbserver.votacao.application.exceptions.CpfInvalidoException;
import org.junit.jupiter.api.Test;

class CpfValidationFakeAdapterTest {

  @Test
  void deveLancarExcecaoQuandoCpfForInvalido() {
    final var adapter = new CpfValidationFakeAdapter();

    final var ex = assertThrows(CpfInvalidoException.class, () -> adapter.validar("123"));
    assertEquals("CPF inválido", ex.getMessage());
  }

  @Test
  void deveRetornarStatusQuandoCpfForValido() {
    final var adapter = new CpfValidationFakeAdapter();
    final var result = adapter.validar("52998224725");

    assertNotNull(result);
  }
}
