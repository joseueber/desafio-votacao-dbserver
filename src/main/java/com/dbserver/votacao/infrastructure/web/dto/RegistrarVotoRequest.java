package com.dbserver.votacao.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistrarVotoRequest(
    @NotBlank @Pattern(regexp = "^\\d{11}$", message = "associadoCpf deve conter 11 dígitos")
        String associadoCpf,
    @NotBlank @Pattern(regexp = "^(SIM|NAO)$", message = "valor deve ser SIM ou NAO")
        String valor) {}
