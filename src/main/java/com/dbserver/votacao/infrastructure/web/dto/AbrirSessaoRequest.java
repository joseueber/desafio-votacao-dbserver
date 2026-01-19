package com.dbserver.votacao.infrastructure.web.dto;

import jakarta.validation.constraints.Min;

public record AbrirSessaoRequest(@Min(1) Long duracaoEmMinutos) {}
