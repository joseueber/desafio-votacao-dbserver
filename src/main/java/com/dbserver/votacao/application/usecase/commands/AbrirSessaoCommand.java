package com.dbserver.votacao.application.usecase.commands;

import java.time.Duration;
import java.util.UUID;

public record AbrirSessaoCommand(UUID pautaId, Duration duracao) {}
