# Sistema de Investimentos – CAIXAVERSO Backend Java

API de simulação de investimentos construída com **Java 21 + Quarkus 3** para o desafio:

> “Painel de Investimentos com Perfil de Risco Dinâmico”

A aplicação:

- Simula investimentos com base em **produtos cadastrados em banco (SQL Server)**.
- Calcula e ajusta o **perfil de risco do cliente** com base em comportamento financeiro.
- Expõe **endpoints REST** para:
    - Simulação de investimentos;
    - Histórico de simulações;
    - Valores simulados por produto/dia;
    - Perfil de risco;
    - Produtos recomendados;
    - Histórico de investimentos;
    - Telemetria (uso da API).
- Usa **JWT** para autenticação e **Redis** para cache de regras e produtos.
- Está pronta para rodar via **Docker / Docker Compose**.

---

## 1. Tecnologias e Stack

- **Linguagem:** Java 21
- **Framework:** Quarkus 3.29.3
- **Banco de Dados:** SQL Server (via `quarkus-jdbc-mssql`)
- **Migração de banco:** Flyway
- **Autenticação:** JWT (RS256, SmallRye JWT)
- **Cache:** Redis + Quarkus Cache
- **Documentação de API:** OpenAPI + Swagger UI
- **Containerização:** Docker & Docker Compose

---

## 2. Arquitetura do Projeto

Arquitetura Hexagonal (Ports & Adapters), garantindo desacoplamento, testabilidade e clareza entre camadas.

- **`br.com.caixaverso.invest.adapter.in.rest`**
    - Resources REST:
        - `AuthResource`, `SimulacaoResource`, `InvestimentoResource`,  
          `PerfilRiscoResource`, `RecomendacaoResource`, `TelemetriaResource`

- **`br.com.caixaverso.invest.application`**
    - `dto`: DTOs de request/response
    - `service`: implementação dos casos de uso
    - `port.in`: portas de entrada (casos de uso expostos)
    - `port.out`: portas de saída (interfaces para infraestrutura — cliente, produtos, investimentos, telemetria, regras)

- **`br.com.caixaverso.invest.domain`**
    - `model`: entidades de domínio (Cliente, Produto, Investimento, Simulação, Telemetria etc.)
    - `constants`: constantes e regras fixas do domínio

- **`br.com.caixaverso.invest.adapter.out`**
    - `repository`: implementações de persistência (Panache/ORM)
    - `telemetria`: persistência de métricas

- **`br.com.caixaverso.invest.infra`**
    - `exception`: tratamento centralizado de erros
    - `filter`: filtros globais (telemetria, MDC etc.)
    - `util`: utilitários gerais (parser de clienteId, mapeadores de risco)

--- 

## 3. Banco de Dados & Migrações

As tabelas e dados iniciais são criados automaticamente via **Flyway**:

- `src/main/resources/db/migration`
    - `V1__create_tables.sql` – criação das tabelas de:
        - Clientes
        - Investimentos
        - Simulações
        - Produtos de investimento
        - Regras de risco, frequência, preferência
        - Registros de telemetria
    - `V2__insert_produtos_parametros_pesos.sql` – produtos de investimento base (Tesouro, CDB, LCI/LCA, Ações, FIIs, ETFs, COEs, Previdência etc.) e regras de risco por produto.
    - `V3__insert_clientes_investimentos_simulacoes.sql` – clientes, investimentos existentes e simulações históricas.
    - `V4__insert_clientes_4_a_10.sql`, `V5__insert_maior_volume_simulacoes.sql` – enriquecimento de cenário.
    - `V6__create_perfil_risco_regra.sql`, `V7__perfil_preferencia_frequencia_regras.sql` – regras para motor de perfil de risco.

Configuração padrão (em `application.properties`):

```properties
quarkus.datasource.db-kind=mssql
quarkus.datasource.jdbc.url=jdbc:sqlserver://sqlserver:1433;encrypt=false;trustServerCertificate=true;databaseName=investimentos
quarkus.datasource.username=sa
quarkus.datasource.password=YourStrong!Passw0rd

quarkus.flyway.migrate-at-start=true
quarkus.flyway.locations=classpath:db/migration
```

---

## 4. Autenticação & Segurança (JWT)

A aplicação usa **JWT (RS256)** com chaves em:

- `src/main/resources/jwt/private.pem`
- `src/main/resources/jwt/public.pem`

Configuração principal:

```properties
quarkus.smallrye-jwt.enabled=true
smallrye.jwt.algorithm=RS256
smallrye.jwt.sign.key.location=jwt/private.pem
smallrye.jwt.verify.key.location=jwt/public.pem
mp.jwt.verify.issuer=caixaverso-investimentos
jwt.expiration.seconds=3600
```

### 4.1. Rotas públicas vs protegidas

Configuração de paths públicos:

```properties
quarkus.http.auth.permission.public.paths=/api/v1/auth/*,/q/swagger-ui/*,/openapi*
```

- **Público (sem token):**
    - `POST /api/v1/auth/login`
    - Swagger UI: `/q/swagger-ui`
    - OpenAPI: `/openapi`

- **Protegido com JWT (ROLE `USER`):**
    - Simulação, histórico, perfil de risco, recomendações, investimentos.

- **Protegido com JWT (ROLE `ADMIN`):**
    - Telemetria.

### 4.2. Credenciais de Exemplo

No endpoint de login:

- `admin / admin` → Roles: `ADMIN`, `USER`
- `user / user` → Role: `USER`

---

## 5. Endpoints da API

### 5.1. Autenticação

#### `POST /api/v1/auth/login`

Request:

```json
{
  "username": "user",
  "password": "user"
}
```

Response (200):

```json
{
  "token": "<JWT>",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

Header de autorização:

```http
Authorization: Bearer <JWT>
```

---

### 5.2. Simulações

Resource: `SimulacaoResource` (`/api/v1`), ROLE `USER`.

#### `POST /api/v1/simular-investimento`

Request (`SimularInvestimentoRequest`):

```json
{
  "clienteId": 123,
  "valor": 10000.00,
  "prazoMeses": 12,
  "tipoProduto": "CDB"
}
```

Response (`SimularInvestimentoResponse`):

```json
{
  "produtoValidado": {
    "id": 101,
    "nome": "CDB Caixa 2026",
    "tipo": "CDB",
    "rentabilidade": 0.12,
    "risco": "Baixo"
  },
  "resultadoSimulacao": {
    "valorFinal": 11200.00,
    "rentabilidadeEfetiva": 0.12,
    "prazoMeses": 12
  },
  "dataSimulacao": "2025-10-31T14:00:00Z"
}
```

#### `GET /api/v1/simulacoes?clienteId={clienteId}`

- Sem parâmetro: lista todas as simulações.
- Com `clienteId`: filtra pelo cliente.

Exemplo de resposta (array de `SimulacaoResumoDTO`):

```json
[
  {
    "id": 1,
    "clienteId": 123,
    "produto": "CDB Caixa 2026",
    "valorInvestido": 10000.00,
    "valorFinal": 11200.00,
    "prazoMeses": 12,
    "dataSimulacao": "2025-10-31T14:00:00Z"
  }
]
```

#### `GET /api/v1/simulacoes/por-produto-dia`

Resumo estatístico por produto e dia:

```json
[
  {
    "produto": "CDB Caixa 2026",
    "data": "2025-10-30",
    "quantidadeSimulacoes": 15,
    "mediaValorFinal": 11050.00
  }
]
```

---

### 5.3. Perfil de Risco

Resource: `PerfilRiscoResource`, ROLE `USER`.

#### `GET /api/v1/perfil-risco/{clienteId}`

Retorna perfil de risco calculado dinamicamente com base em:

- Volume de investimentos
- Frequência de movimentações
- Preferência por liquidez vs rentabilidade (regras parametrizadas em banco)

Exemplo de resposta:

```json
{
  "clienteId": 123,
  "perfil": "Moderado",
  "pontuacao": "65",
  "descricao": "Perfil equilibrado entre segurança e rentabilidade."
}
```

---

### 5.4. Produtos Recomendados

Resource: `RecomendacaoResource`, ROLE `USER`.

#### `GET /api/v1/clientes/{clienteId}/recomendacoes`

- Usa o motor de recomendação + perfil calculado do cliente.

#### `GET /api/v1/produtos-recomendados/{perfil}`

Exemplo:

```json
[
  {
    "id": 101,
    "nome": "CDB Caixa 2026",
    "tipo": "CDB",
    "rentabilidade": 0.12,
    "risco": "Baixo"
  },
  {
    "id": 102,
    "nome": "Fundo XPTO",
    "tipo": "Fundo",
    "rentabilidade": 0.18,
    "risco": "Alto"
  }
]
```

---

### 5.5. Histórico de Investimentos

Resource: `InvestimentoResource`, ROLE `USER`.

#### `GET /api/v1/investimentos/{clienteId}`

Retorna os investimentos realizados por um cliente:

```json
[
  {
    "id": 1,
    "tipo": "CDB",
    "valor": 5000.00,
    "rentabilidade": 0.12,
    "data": "2025-01-15"
  }
]
```

---

### 5.6. Telemetria da API

Resource: `TelemetriaResource`, ROLE `ADMIN`.

#### `GET /api/v1/telemetria`

Retorna dados de uso dos endpoints (registrados via `RequestMetricsFilter`):

```json
{
  "servicos": [
    {
      "nome": "simular-investimento",
      "quantidadeChamadas": 120,
      "mediaTempoRespostaMs": 250
    },
    {
      "nome": "perfil-risco",
      "quantidadeChamadas": 80,
      "mediaTempoRespostaMs": 180
    }
  ],
  "periodo": {
    "inicio": "2025-10-01",
    "fim": "2025-10-31"
  }
}
```

---

## 6. Cache com Redis

Uso de cache para melhorar performance de consultas de regras e produtos.

Repositórios anotados com `@CacheResult`, exemplo:

- `ProdutoInvestimentoRepository`:
    - Cache `"produtos-todos"`
    - Cache `"produtos-por-tipo"`

Configuração:

```properties
quarkus.redis.hosts=redis://localhost:6379

quarkus.cache.redis.expire-after-write=10m
quarkus.cache.redis."frequencia-invest-regras".expire-after-write=24h
quarkus.cache.redis."frequencia-sim-regras".expire-after-write=24h
quarkus.cache.redis."produto-risco-regras".expire-after-write=24h
quarkus.cache.redis."produtos-todos".expire-after-write=10m
quarkus.cache.redis."produtos-por-tipo".expire-after-write=10m
```

No Docker Compose, o host é sobrescrito com:

```yaml
environment:
  - QUARKUS_REDIS_HOSTS=redis://redis:6379
```

---

## 7. Documentação Swagger / OpenAPI

Configuração em `application.properties`:

```properties
quarkus.smallrye-openapi.path=/openapi
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.enable=true
quarkus.swagger-ui.path=/q/swagger-ui
```

Acessos:

- **Swagger UI:**  
  http://localhost:8080/q/swagger-ui

- **OpenAPI JSON/YAML:**  
  http://localhost:8080/openapi

---

## 8. Como Executar

### 8.1. Requisitos

- Java 21
- Maven (ou usar o `./mvnw` do projeto)
- Docker + Docker Compose

### 8.2. Rodando tudo via Docker Compose

Na raiz do projeto:

```bash
# build da imagem da API
./mvnw clean package -DskipTests

# sobe SQL Server, Redis e API
docker compose up -d
```

Serviços:

- API: http://localhost:8080
- SQL Server: `localhost:1433`
- Redis: `localhost:6379`

---

## 9. Massa de Teste – Parâmetros para o Avaliador

A base já sobe com uma massa de **clientes**, **produtos**, **investimentos**, **simulações** e **regras de risco**.  
A ideia é que o avaliador possa **brincar livremente** com as combinações de:

- `clienteId`
- `perfil` (CONSERVADOR, MODERADO, AGRESSIVO)
- `tipoProduto`

Não existe vínculo fixo entre um cliente e um perfil ou tipo de produto na documentação:  
o perfil é calculado dinamicamente pelo motor, e o avaliador pode explorar qualquer combinação nos testes.

### 9.1. Clientes (clientId)

Os clientes estão na tabela `Cliente`, com `id` numérico sequencial a partir de `1`.

Em chamadas da API, o campo `clienteId` deve usar esse `id`, por exemplo:

- `GET /api/v1/perfil-risco/1`
- `GET /api/v1/investimentos/2`
- `POST /api/v1/simular-investimento` usando `"clienteId": 3`

O avaliador pode usar qualquer `clienteId` existente para testar o comportamento do motor e outras funcionalidades.
Massa de testes criada na base com clientId entre 1 e 15. 

### 9.2. Tipos de Produto (`tipoProduto`)

Os produtos de investimento estão na tabela `ProdutoInvestimento`.  
O campo `tipo` define a categoria do produto, com valores como:

- `TESOURO`
- `CDB`
- `LCI`
- `LCA`
- `FII`
- `FUNDO`
- `ACAO`
- `ETF`
- `COE`
- `DEBENTURE`
- `PREVIDENCIA`

No endpoint de simulação, o avaliador pode informar, por exemplo:

```json
{
  "clienteId": 1,
  "valor": 5000.00,
  "prazoMeses": 12,
  "tipoProduto": "CDB"
}
```

mas é livre para testar qualquer combinação de `clienteId` com qualquer `tipoProduto` existente.

### 9.3. Intervalos de Score para Perfil de Risco (parametrizáveis)

O motor de recomendação calcula um **score de risco** de 0 a 100 para o cliente.  
Esse score é classificado em perfil de risco usando a tabela `PerfilRiscoRegra`.

Configuração padrão de faixas na base:

| Ordem | Score mín | Score máx | Perfil      |
|:-----:|:---------:|:---------:|-------------|
| 1     | 0         | 40        | CONSERVADOR |
| 2     | 41        | 70        | MODERADO    |
| 3     | 71        | 100       | AGRESSIVO   |

Esses **intervalos podem ser alterados diretamente no banco** pelo gestor (por exemplo, via `UPDATE` na tabela `PerfilRiscoRegra`), sem mudança de código.  
Qualquer ajuste de limites impacta imediatamente a classificação de perfil devolvida pelo endpoint `/api/v1/perfil-risco/{clienteId}` e, consequentemente, as recomendações de produtos.

### 9.4. Outros parâmetros configuráveis do motor

Além das faixas de score, o motor utiliza outras tabelas de parametrização que também ficam na base e podem ser ajustadas pelo gestor para calibrar o comportamento:

- `ProdutoRiscoRegra` – faixas de rentabilidade mínima/máxima x nível de risco do produto.
- `perfil_freq_invest_regra` – parametrização de pesos/fatores por frequência de investimentos realizados.
- `perfil_freq_simulacao_regra` – parâmetros por frequência de simulações.
- `perfil_preferencia_regra` – regras ligadas à preferência por liquidez x rentabilidade.

Os valores atuais dessas tabelas são carregados pelos scripts Flyway em `db/migration` e podem ser ajustados conforme a política de investimento desejada.  
O código da API apenas lê esses parâmetros, permitindo que o motor de recomendação seja **totalmente configurável em banco**.

