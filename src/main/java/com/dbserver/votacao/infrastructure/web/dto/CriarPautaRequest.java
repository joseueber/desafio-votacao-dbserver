package com.dbserver.votacao.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarPautaRequest(
    @NotBlank @Size(max = 200) String titulo, @Size(max = 1000) String descricao) {}
