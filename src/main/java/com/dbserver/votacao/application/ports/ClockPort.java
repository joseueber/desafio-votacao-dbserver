package com.dbserver.votacao.application.ports;

import java.time.Instant;

public interface ClockPort {
  Instant agora();
}
