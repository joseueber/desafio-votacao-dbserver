package com.dbserver.votacao.infrastructure.web.dto;

import java.time.Instant;
import java.util.UUID;

public record AbrirSessaoResponse(
    UUID id, UUID pautaId, Instant abertaEm, Instant fechaEm, String status) {}
