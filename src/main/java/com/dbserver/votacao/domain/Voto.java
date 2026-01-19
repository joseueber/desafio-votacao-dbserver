package com.dbserver.votacao.domain;

import com.dbserver.votacao.domain.enums.VotoValor;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
public final class Voto {

  private final UUID id;
  private final UUID pautaId;
  private final String associadoCpf;
  private final VotoValor valor;
  private final Instant criadoEm;

  public Voto(UUID id, UUID pautaId, String associadoCpf, VotoValor valor, Instant criadoEm) {
    this.id = Objects.requireNonNull(id);
    this.pautaId = Objects.requireNonNull(pautaId);
    this.associadoCpf = validarCpf(associadoCpf);
    this.valor = Objects.requireNonNull(valor);
    this.criadoEm = Objects.requireNonNull(criadoEm);
  }

  private static String validarCpf(String cpf) {
    return Optional.ofNullable(cpf)
        .orElseThrow(() -> new IllegalArgumentException("associadoCpf não pode ser nulo"))
        .replaceAll("\\D", "")
        .transform(
            digits -> {
              if (digits.length() != 11) {
                throw new IllegalArgumentException("associadoCpf deve conter 11 dígitos");
              }
              return digits;
            });
  }
}
