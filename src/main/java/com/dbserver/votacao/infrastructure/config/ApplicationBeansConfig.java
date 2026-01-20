package com.dbserver.votacao.infrastructure.config;

import com.dbserver.votacao.application.ports.ClockPort;
import com.dbserver.votacao.application.ports.CpfValidationPort;
import com.dbserver.votacao.application.ports.PautaRepositoryPort;
import com.dbserver.votacao.application.ports.SessaoRepositoryPort;
import com.dbserver.votacao.application.ports.VotoRepositoryPort;
import com.dbserver.votacao.application.usecase.AbrirSessaoUseCase;
import com.dbserver.votacao.application.usecase.CadastrarPautaUseCase;
import com.dbserver.votacao.application.usecase.ConsultarResultadoUseCase;
import com.dbserver.votacao.application.usecase.RegistrarVotoUseCase;
import java.time.Instant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeansConfig {

  @Bean
  public ClockPort clockPort() {
    return Instant::now;
  }

  @Bean
  public CadastrarPautaUseCase cadastrarPautaUseCase(
      PautaRepositoryPort pautaRepositoryPort, ClockPort clockPort) {
    return new CadastrarPautaUseCase(pautaRepositoryPort, clockPort);
  }

  @Bean
  public AbrirSessaoUseCase abrirSessaoUseCase(
      PautaRepositoryPort pautaRepositoryPort,
      SessaoRepositoryPort sessaoRepositoryPort,
      ClockPort clockPort) {
    return new AbrirSessaoUseCase(pautaRepositoryPort, sessaoRepositoryPort, clockPort);
  }

  @Bean
  public RegistrarVotoUseCase registrarVotoUseCase(
      PautaRepositoryPort pautaRepositoryPort,
      SessaoRepositoryPort sessaoRepositoryPort,
      VotoRepositoryPort votoRepositoryPort,
      CpfValidationPort cpfValidationPort,
      ClockPort clockPort) {
    return new RegistrarVotoUseCase(
        pautaRepositoryPort,
        sessaoRepositoryPort,
        votoRepositoryPort,
        cpfValidationPort,
        clockPort);
  }

  @Bean
  public ConsultarResultadoUseCase consultarResultadoUseCase(
      PautaRepositoryPort pautaRepositoryPort, VotoRepositoryPort votoRepositoryPort) {
    return new ConsultarResultadoUseCase(pautaRepositoryPort, votoRepositoryPort);
  }
}
