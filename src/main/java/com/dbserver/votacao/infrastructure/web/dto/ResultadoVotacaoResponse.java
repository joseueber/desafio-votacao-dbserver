package com.dbserver.votacao.infrastructure.web.dto;

import java.util.UUID;

public record ResultadoVotacaoResponse(UUID pautaId, long totalSim, long totalNao, long total) {}
