package com.dbserver.votacao.infrastructure.integration;

import com.dbserver.votacao.application.exceptions.CpfInvalidoException;
import com.dbserver.votacao.application.ports.CpfValidationPort;
import java.security.SecureRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CpfValidationFakeAdapter implements CpfValidationPort {

  private final SecureRandom random = new SecureRandom();

  @Override
  public CpfValidationResult validar(String cpf) {
    log.debug("Validando CPF (Fake): {}", cpf);
    final String digits = onlyDigits(cpf);

    if (!isValidCpf(digits)) {
      log.warn("CPF inválido informado: {}", cpf);
      throw new CpfInvalidoException("CPF inválido");
    }

    var result =
        random.nextBoolean()
            ? CpfValidationResult.ABLE_TO_VOTE
            : CpfValidationResult.UNABLE_TO_VOTE;
    log.debug("Resultado da validação do CPF {}: {}", cpf, result);
    return result;
  }

  private static String onlyDigits(String value) {
    return value == null ? "" : value.replaceAll("\\D", "");
  }

  private static boolean isValidCpf(String cpf) {
    if (cpf == null || cpf.length() != 11) return false;

    // rejeita sequências repetidas
    char first = cpf.charAt(0);
    boolean allEqual = true;
    for (int i = 1; i < cpf.length(); i++) {
      if (cpf.charAt(i) != first) {
        allEqual = false;
        break;
      }
    }
    if (allEqual) return false;

    int d1 = calcDigit(cpf, 10);
    int d2 = calcDigit(cpf, 11);

    return (cpf.charAt(9) - '0') == d1 && (cpf.charAt(10) - '0') == d2;
  }

  private static int calcDigit(String cpf, int weightStart) {
    int sum = 0;
    int weight = weightStart;
    int limit = weightStart == 10 ? 9 : 10;

    for (int i = 0; i < limit; i++) {
      sum += (cpf.charAt(i) - '0') * weight--;
    }

    int mod = sum % 11;
    return (mod < 2) ? 0 : (11 - mod);
  }
}
