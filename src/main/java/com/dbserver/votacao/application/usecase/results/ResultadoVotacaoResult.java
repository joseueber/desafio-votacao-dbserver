package com.dbserver.votacao.application.usecase.results;

import java.util.UUID;

public record ResultadoVotacaoResult(UUID pautaId, long totalSim, long totalNao, long total) {}
