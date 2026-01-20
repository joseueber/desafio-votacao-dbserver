package com.dbserver.votacao.infrastructure.web.handler;

import com.dbserver.votacao.application.exceptions.CpfInvalidoException;
import com.dbserver.votacao.application.exceptions.RecursoNaoEncontradoException;
import com.dbserver.votacao.application.exceptions.RegraDeNegocioException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(RecursoNaoEncontradoException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(RecursoNaoEncontradoException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("timestamp", Instant.now().toString(), "erro", ex.getMessage()));
  }

  @ExceptionHandler(RegraDeNegocioException.class)
  public ResponseEntity<Map<String, Object>> handleBusiness(RegraDeNegocioException ex) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(Map.of("timestamp", Instant.now().toString(), "erro", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            Map.of(
                "timestamp",
                Instant.now().toString(),
                "erro",
                "Requisição inválida",
                "detalhes",
                ex.getBindingResult().getFieldErrors().stream()
                    .map(f -> f.getField() + ": " + f.getDefaultMessage())
                    .toList()));
  }

  @ExceptionHandler(CpfInvalidoException.class)
  public ResponseEntity<Map<String, Object>> handleCpfInvalido(CpfInvalidoException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("timestamp", Instant.now().toString(), "erro", ex.getMessage()));
  }
}
