package com.dbserver.votacao.application.usecase.commands;

import com.dbserver.votacao.domain.enums.VotoValor;
import java.util.UUID;

public record RegistrarVotoCommand(UUID pautaId, String associadoCpf, VotoValor valor) {}
