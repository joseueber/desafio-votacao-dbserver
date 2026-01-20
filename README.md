# Desafio Técnico – Votação (DBServer)

Este projeto implementa uma API REST para gerenciamento de pautas, sessões de votação e votos, conforme o desafio técnico proposto pela DBServer.

A solução foi pensada com foco em qualidade de código, clareza arquitetural e decisões técnicas justificáveis, considerando cenários reais de produção, como concorrência e grande volume de dados.

---

## Visão Geral da Solução

A aplicação permite:
- cadastrar pautas
- abrir sessões de votação para uma pauta
- registrar votos de associados
- consultar o resultado da votação

O projeto foi estruturado para suportar centenas de milhares de votos, priorizando:
- integridade dos dados
- consistência sob concorrência
- desempenho na apuração dos resultados
- facilidade de evolução e manutenção

---

## Arquitetura

Foi adotada Arquitetura Hexagonal (Ports and Adapters), inspirada nos princípios da Clean Architecture.

Estrutura de pacotes:

```
domain/
 ├─ model
 ├─ enums
 └─ regras de negócio

application/
 ├─ usecase
 ├─ ports
 └─ exceptions

infrastructure/
 ├─ persistence
 ├─ web
 └─ integration
```

Motivações:
- domínio independente de frameworks
- regras de negócio protegidas e testáveis
- facilidade para troca de banco, web ou integrações externas

As entidades de domínio são ricas, contendo validações e invariantes próprias.

---

## Decisões Técnicas Importantes

### Identificadores
- Todos os IDs são UUID
- A geração ocorre no banco de dados (PostgreSQL) via uuid_generate_v4()

Justificativa:
Identificadores persistentes são responsabilidade da camada de persistência, não da aplicação.

---

### Banco de Dados
- PostgreSQL
- Versionamento com Flyway
- Não utilização de H2

Motivo:
H2 não replica fielmente tipos, constraints, funções e comportamento transacional do PostgreSQL.

---

### Concorrência e Integridade
A regra “um associado vota apenas uma vez por pauta” é garantida no banco:

UNIQUE (pauta_id, associado_cpf)

Benefícios:
- elimina consultas prévias antes do insert
- evita race conditions
- delega integridade ao banco

---

## Tecnologias Utilizadas
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Gradle (Groovy DSL)
- Lombok
- Swagger / OpenAPI
- Docker Compose

---

## Como Executar o Projeto

Pré-requisitos:
- Java 17+
- Docker e Docker Compose

Passos:
1. docker compose up -d
2. ./gradlew bootRun
3. Acessar http://localhost:8080/swagger-ui.html

---

## Endpoints Principais

POST /api/v1/pautas  
POST /api/v1/pautas/{pautaId}/sessao  
POST /api/v1/pautas/{pautaId}/votos  
GET  /api/v1/pautas/{pautaId}/resultado

---

## Testes
- Testes unitários focados em domínio e casos de uso
- Uso de mocks para isolamento das regras
- Testes de integração com banco avaliados e removidos por dependência de ambiente Docker no Windows

---

## Tarefa Bônus 1 – Validação Externa de CPF
- Implementado client fake via adapter
- CPF inválido retorna 404
- CPF válido retorna ABLE_TO_VOTE ou UNABLE_TO_VOTE de forma aleatória
- CPF válido porém não habilitado retorna 422

---

## Tarefa Bônus 2 – Performance
- Índice composto para apuração: (pauta_id, valor)
- Queries agregadas no banco
- Eliminação de leituras desnecessárias antes do insert
- Uso de constraints para garantir integridade

---

## Tarefa Bônus 3 – Versionamento
A API é versionada de forma explícita no caminho (URI versioning):
```
/api/v1/...
/api/v2/...
```
### Estratégia adotada
- A versão atual da aplicação é exposta em /api/v1
- Novas versões (/api/v2, /api/v3, etc.) são criadas apenas quando há mudanças que quebram contrato
- Versões anteriores permanecem estáveis, recebendo apenas correções compatíveis

Essa abordagem foi escolhida por:

- ser simples e clara para os consumidores da API
- facilitar documentação via Swagger/OpenAPI
- permitir roteamento direto em gateways e proxies
- não depender de negociação via headers

---

## Considerações Finais
O projeto prioriza clareza, domínio bem modelado, decisões técnicas justificadas e preparação para cenários reais de produção.
