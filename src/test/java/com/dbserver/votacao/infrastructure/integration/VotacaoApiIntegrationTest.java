package com.dbserver.votacao.infrastructure.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dbserver.votacao.application.ports.CpfValidationPort;
import com.dbserver.votacao.infrastructure.web.dto.AbrirSessaoRequest;
import com.dbserver.votacao.infrastructure.web.dto.AbrirSessaoResponse;
import com.dbserver.votacao.infrastructure.web.dto.CriarPautaRequest;
import com.dbserver.votacao.infrastructure.web.dto.CriarPautaResponse;
import com.dbserver.votacao.infrastructure.web.dto.RegistrarVotoRequest;
import com.dbserver.votacao.infrastructure.web.dto.RegistrarVotoResponse;
import com.dbserver.votacao.infrastructure.web.dto.ResultadoVotacaoResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(VotacaoApiIntegrationTest.TestCpfValidationConfig.class)
class VotacaoApiIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void shouldCreatePautaOpenSessionVoteAndGetResult() {
    var pautaResponse =
        restTemplate.postForEntity(
            "/api/v1/pautas",
            new CriarPautaRequest("Pauta Integracao", "Descricao teste"),
            CriarPautaResponse.class);

    assertThat(pautaResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(pautaResponse.getBody()).isNotNull();

    var pautaId = pautaResponse.getBody().id();

    var sessaoResponse =
        restTemplate.postForEntity(
            "/api/v1/pautas/" + pautaId + "/sessao",
            new AbrirSessaoRequest(5L),
            AbrirSessaoResponse.class);

    assertThat(sessaoResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(sessaoResponse.getBody()).isNotNull();
    assertThat(sessaoResponse.getBody().pautaId()).isEqualTo(pautaId);
    assertThat(sessaoResponse.getBody().status()).isEqualTo("ABERTA");

    var votoResponse =
        restTemplate.postForEntity(
            "/api/v1/pautas/" + pautaId + "/votos",
            new RegistrarVotoRequest("12345678901", "SIM"),
            RegistrarVotoResponse.class);

    assertThat(votoResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(votoResponse.getBody()).isNotNull();

    var resultadoResponse =
        restTemplate.getForEntity(
            "/api/v1/pautas/" + pautaId + "/resultado", ResultadoVotacaoResponse.class);

    assertThat(resultadoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resultadoResponse.getBody()).isNotNull();
    assertThat(resultadoResponse.getBody().pautaId()).isEqualTo(pautaId);
    assertThat(resultadoResponse.getBody().totalSim()).isEqualTo(1);
    assertThat(resultadoResponse.getBody().totalNao()).isEqualTo(0);
    assertThat(resultadoResponse.getBody().total()).isEqualTo(1);
  }

  @Test
  void shouldRejectDuplicateVote() {
    var pautaResponse =
        restTemplate.postForEntity(
            "/api/v1/pautas",
            new CriarPautaRequest("Pauta Duplicada", "Descricao teste"),
            CriarPautaResponse.class);

    assertThat(pautaResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(pautaResponse.getBody()).isNotNull();
    var pautaId = pautaResponse.getBody().id();

    restTemplate.postForEntity(
        "/api/v1/pautas/" + pautaId + "/sessao",
        new AbrirSessaoRequest(5L),
        AbrirSessaoResponse.class);

    restTemplate.postForEntity(
        "/api/v1/pautas/" + pautaId + "/votos",
        new RegistrarVotoRequest("12345678901", "SIM"),
        RegistrarVotoResponse.class);

    var duplicateResponse =
        restTemplate.postForEntity(
            "/api/v1/pautas/" + pautaId + "/votos",
            new RegistrarVotoRequest("12345678901", "NAO"),
            Map.class);

    assertThat(duplicateResponse.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    assertThat(duplicateResponse.getBody()).isNotNull();
    assertThat(duplicateResponse.getBody().get("erro")).isEqualTo("CPF já votou nesta pauta");
  }

  @TestConfiguration
  static class TestCpfValidationConfig {
    @Bean
    @Primary
    public CpfValidationPort cpfValidationPort() {
      return cpf -> CpfValidationPort.CpfValidationResult.ABLE_TO_VOTE;
    }
  }
}
