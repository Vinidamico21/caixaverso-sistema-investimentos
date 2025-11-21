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
    - `enums`: enums de domínio (`PerfilRisco`, `PreferenciaPerfil`, `RiscoProduto`)
    - `constants`: constantes e regras fixas do domínio (descrições de perfil, pesos padrão, limites, mensagens)

- **`br.com.caixaverso.invest.adapter.out`**
    - `repository`: implementações de persistência (Panache/ORM)
    - `telemetria`: persistência de métricas

- **`br.com.caixaverso.invest.infra`**
    - `entities`: entidades JPA (Cliente, ProdutoInvestimento, Investimento, SimulacaoInvestimento, Regras, TelemetriaRegistro)
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

## 4.1. Credenciais para testes

Para facilitar a avaliação do projeto, estão disponíveis dois perfis de acesso pré-configurados:

| **Usuário** | **Senha** | **Roles**        |
|-------------|-----------|------------------|
| `admin`     | `admin@123teste`   | `ADMIN`, `USER` |
| `user`      | `user@123teste`    | `USER`          |


Essas credenciais devem ser usadas no endpoint:

POST /api/v1/auth/login


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

### 4.2. Rotas públicas vs protegidas

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

## 4.3. Mecanismo Anti-Bot: Tentativas e Bloqueio Temporário

A aplicação conta com uma proteção interna contra ataques de força bruta (brute force) e tentativas automatizadas (bots) no endpoint de login.

- Controle de tentativas

O sistema registra falhas consecutivas de autenticação por usuário.
- Bloqueio temporário (lockout)

Após exceder 5 tentativas falhas, o usuário é automaticamente bloqueado por 5 minutos.

Durante esse período:

O login é rejeitado imediatamente

Bloqueia tentativas automatizadas de senhas

Impede exploração contínua por bots

Mantém o serviço estável sob ataque
- Limpeza automática

Se o login for realizado com sucesso, o contador de tentativas é apagado.

- Arquitetura baseada em Port/Adapter

Implementação seguindo o padrão de Arquitetura Hexagonal:

Port: TentativaLoginPort

Adapter: TentativaLoginInMemoryAdapter

Sendo facilmente substituível por Redis, banco relacional ou cache distribuído.

## 4.4. Benefícios dessa Proteção
- Prevenção contra ataques de força bruta

Bots não conseguem testar senhas indefinidamente.

- Maior estabilidade e resiliência

Reduz requisições abusivas, protegendo o desempenho da API.

- Aumento da segurança geral

Mesmo usando credenciais simples para demonstração, o endpoint não fica exposto a exploração.

- Arquitetura extensível

A implementação em Port/Adapter permite evolução futura para:

 - Redis

 - Banco relacional

 - Sistemas distribuídos de rate limit

 - Facilita a avaliação do projeto

O avaliador dispõe de usuários prontos e percebe que o sistema contempla medidas reais de segurança e mitigação contra ataques.

## 5. Endpoints da API

### 5.1. Autenticação

#### `POST /api/v1/auth/login`

Request:

```json
{
  "username": "user",
  "password": "user@123teste"
}
```

```json
{
  "username": "admin",
  "password": "admin@123teste"
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

#### `GET /api/v1/simulacoes?clienteId={clienteId}&page={page}&size={size}`

- clienteId (opcional) – filtra por cliente.
- page (opcional, padrão = 0) – número da página (0-based).
- size (opcional, padrão = 20) – quantidade de registros por página.

Exemplo de resposta (array de `SimulacaoResumoDTO`):

```json
[
  {
    "content": [
      {
        "id": 1,
        "clienteId": 123,
        "produto": "CDB Caixa 2026",
        "valorInvestido": 10000.00,
        "valorFinal": 11200.00,
        "prazoMeses": 12,
        "dataSimulacao": "2025-10-31T14:00:00Z"
      }
    ],
    "page": 0,
    "size": 20,
    "totalElements": 1,
    "totalPages": 1
  } 
]
```

#### `GET /api/v1/simulacoes/por-produto-dia`

Resumo estatístico por produto e dia, **com paginação em memória**.

**Parâmetros de query:**

- `page` (opcional, padrão = `0`) – página desejada (0-based)
- `size` (opcional, padrão = `10`) – quantidade de registros por página

Exemplo:

```http
GET /api/v1/simulacoes/por-produto-dia?page=0&size=10
Authorization: Bearer <JWT>
```

```json
{
  "content": [
    {
      "produto": "CDB Caixa 2026",
      "data": "2025-10-30",
      "quantidadeSimulacoes": 15,
      "mediaValorFinal": 11050.00
    },
    {
      "produto": "Tesouro Selic 2027",
      "data": "2025-10-30",
      "quantidadeSimulacoes": 8,
      "mediaValorFinal": 10320.50
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3
}
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

#### `GET /api/v1/investimentos/{clienteId}?page={page}&size={size}`

- clienteId (obrigatório) – ID do cliente.
- page (opcional, padrão = 0) – número da página (0-based).
- size (opcional, padrão = 20) – quantidade de registros por página.

Retorna os investimentos realizados por um cliente:

```json
[
  {
    "content": [
      {
        "id": 1,
        "tipo": "CDB",
        "valor": 5000.00,
        "rentabilidade": 0.12,
        "data": "2025-01-15"
      },
      {
        "id": 2,
        "tipo": "Tesouro Selic",
        "valor": 2000.00,
        "rentabilidade": 0.10,
        "data": "2025-02-10"
      }
    ],
    "page": 0,
    "size": 20,
    "totalElements": 2,
    "totalPages": 1
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

```bash
# Para derrubar containers + volumes:
docker compose down -v

# Derrubar tudo (volumes, containers, imagens):
docker compose down --rmi all --volumes --remove-orphans
```

- Serviços:
    - API: http://localhost:8080
    - SQL Server: `localhost:1433`
    - Redis: `localhost:6379`

---

### 8.3. Usando a imagem do Docker Hub

```bash
# Puxar a imagem oficial
docker pull vinipereira21/caixaverso-sistema-investimentos:latest
```

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

---

## 10. Funcionamento do Motor de Recomendação (Resumo Técnico)

O motor de recomendação combina **comportamento do cliente**, **características dos produtos** e **regras parametrizadas em banco** para montar uma lista de produtos alinhados ao perfil de risco.

### 10.1. Cálculo do Perfil de Risco do Cliente

O perfil do cliente (enum `PerfilRisco`: `CONSERVADOR`, `MODERADO`, `AGRESSIVO`, `DESCONHECIDO`) é derivado de um **score de risco** calculado a partir de três componentes:

1. **Preferência (Liquidez vs Rentabilidade)**
    - Analisa as simulações do cliente e verifica se ele tende a escolher produtos:
        - com **alta liquidez**; ou
        - com **alta rentabilidade**.
    - A partir desse padrão, classifica em um enum `PreferenciaPerfil`:
        - `LIQUIDEZ`
        - `RENTABILIDADE`
        - `EMPATE`
    - A pontuação associada é buscada em banco (`perfil_preferencia_regra`).

2. **Volume Investido(Total valor aplicado)**
    - Soma os valores investidos e gera uma pontuação de acordo com faixas (por exemplo, acima de 50k recebe pontuação maior que acima de 10k).
    - Esse componente mede **capacidade financeira do cliente**.

3. **Frequência de Movimentações (Simulações + Investimentos)**
- Considera:
    - **Frequência de Investimentos Reais**: quantidade de investimentos reais realizados nos últimos 12 meses
    - **Frequência de Simulações**: quantidade de simulações realizadas nos últimos 12 meses
    - **O range de 12 meses pode ser alterado de acordo com a definição do Gestor**
- Cada uma dessas frequências é convertida em pontos via tabelas de regras:
    - `perfil_freq_invest_regra`: tabela que mapeia a quantidade de investimentos reais para pontuação
    - `perfil_freq_simulacao_regra`: tabela que mapeia a quantidade de simulações para pontuação
- **Cálculo**:
    - Busca todos os investimentos reais do cliente nos últimos 12 meses via `investimentoPort.findByClienteIdAndPeriodo()`
    - Filtra todas as simulações do cliente nos últimos 12 meses via `simulacaoPort.listar()` com filtro por data
    - Obtém a pontuação para quantidade de investimentos via `freqInvestRegraPort.buscarPontuacao(qtdInvestimentos)`
    - Obtém a pontuação para quantidade de simulações via `freqSimRegraPort.buscarPontuacao(qtdSimulacoes)`
    - **Pontuação Total de Frequência** = Soma das pontuações de investimentos reais + simulações




O **score final** é a soma:

```text
scoreFinal = scorePreferencia + scoreVolume + scoreFrequencia
```

Esse valor é então convertido em perfil através da tabela `PerfilRiscoRegra` (faixas de score).

### 10.2. Classificação de Risco dos Produtos

Cada produto tem:

- um tipo (CDB, TESOURO, FII, ETF etc.);
- uma taxa anual de rentabilidade (`taxaAnual`);
- uma informação de liquidez/prazo.

O motor consulta a tabela `ProdutoRiscoRegra` para transformar rentabilidade em um enum `RiscoProduto`:

- `BAIXO`
- `MEDIO`
- `ALTO`

Essa classificação é usada tanto para exibição quanto para o cálculo do score final de recomendação.

### 10.3. Cálculo do Score de Recomendação por Produto

Para cada produto elegível, o motor calcula um **score numérico**, considerando três eixos:

1. **Rentabilidade**
    - Se a taxa anual é:
        - abaixo de um limiar → pontuação baixa;
        - dentro de uma faixa intermediária → pontuação média;
        - acima do limiar superior → pontuação alta.

2. **Liquidez / Prazo**
    - Liquidez diária / D+0 / prazos curtos → pontuação maior.
    - Liquidez mensal / D+30 / prazos médios → pontuação intermediária.
    - Prazos longos / baixa liquidez → pontuação menor.

3. **Compatibilidade de Risco (Produto x Perfil)**
    - Exemplo:
        - Cliente **CONSERVADOR** → prefere produtos `BAIXO` risco.
        - Cliente **MODERADO** → aceita `BAIXO` e `MEDIO` com bom peso.
        - Cliente **AGRESSIVO** → favorece produtos `ALTO` risco.

Cada um desses eixos gera um score (`liq`, `rent`, `risco`) e, em seguida, o motor combina com **pesos por perfil**, definidos em constantes no domínio:

- Perfil CONSERVADOR → peso maior em **liquidez**, menor em rentabilidade.
- Perfil MODERADO → pesos equilibrados.
- Perfil AGRESSIVO → peso maior em **rentabilidade** e compatibilidade de risco.

O cálculo simplificado:

```text
scoreProduto = (liq * pesoLiquidezPerfil)
             + (rent * pesoRentabilidadePerfil)
             + (risco * pesoCompatibilidadePerfil)
```

Por fim:

- Os produtos são ordenados por `scoreProduto` (decrescente).
- Apenas os **melhores N produtos** (ex.: Top 5) são retornados na recomendação.

Isso garante que o avaliador veja recomendações **coerentes com o perfil do cliente**, mas totalmente **parametrizáveis via banco**, sem necessidade de alterar código.


### 11 - Estrutura de Testes

O projeto possui uma suíte de testes bem estruturada, espelhando a arquitetura da aplicação.

#### Tecnologias Utilizadas

- **JUnit 5** para testes unitários e de integração
- **Mockito** para criação de mocks e stubs nas camadas de serviço e portas
- **Quarkus Test + RestAssured** para testes de recursos REST (testando a API “de fora pra dentro”)
- **JaCoCo** para medição de cobertura de código (`target/site/jacoco`)

#### Organização por Camadas (`src/test/java`)

- **application**: testes de *services*, *ports* e *DTOs*, garantindo regras de negócio e contratos das portas
- **domain**: testes de entidades, *enums* e regras de domínio
- **infra**: testes de *adapters*, repositórios, mapeamentos e *exception mappers*
- **resource**: testes dos *controllers/resources* REST, validando status HTTP, payloads e cenários de erro

#### Cobertura

- Cobertura de linhas **acima de 90%**, com boa distribuição entre as camadas
- Foco tanto em **fluxos felizes** quanto em **cenários de erro e validações**

Essa estrutura garante testes alinhados ao modelo de **Clean Architecture / DDD**, com alta cobertura, boa separação de responsabilidades e facilidade de manutenção e evolução da base de código.

## 12. Performance

A API foi estruturada para responder de forma rápida e estável mesmo com aumento de volume de dados, combinando **cache**, **consultas otimizadas** e **processamento em memória**.

### Cache Estratégico (Redis + Quarkus Cache)

- Uso de **Quarkus Cache + Redis** (`quarkus-redis-cache`) para evitar consultas repetitivas ao banco.
- Em cache ficam:
    - Regras de negócio (perfil de risco, frequência, risco por produto, preferências).
    - Listas de produtos de investimento (lista completa e por tipo).
- Os dados são carregados uma vez e reutilizados enquanto o **TTL** estiver válido, reduzindo drasticamente acessos ao banco.

### Consultas Otimizadas ao Banco

- Repositórios **Panache** consultam diretamente pelas colunas mais utilizadas em filtros e relacionamentos.
- O script de schema inclui **índices** para:
    - FKs: `cliente_id`, `produto_id`
    - Datas: `data_simulacao_dia`
    - Status: `status`
    - Propriedades de produto: `risco`, `ativo`, `tipo`
    - Telemetria: `endpoint`, `data_registro`
- Isso reduz *full table scans* e acelera consultas de histórico, simulações e relatórios.

### Motor de Recomendação em Memória

- O `MotorRecomendacaoService` trabalha sobre **produtos e regras já carregados/cacheados**.
- O cálculo de perfil, aplicação de preferências e classificação de risco ocorre 100% em memória usando **Java Streams**.
- Durante o fluxo de recomendação **não há novas idas ao banco**, garantindo respostas em **milissegundos**.

### Lazy Loading nas Entidades

Os relacionamentos em `Investimento` e `SimulacaoInvestimento` utilizam *lazy loading*, evitando carga desnecessária de entidades relacionadas:

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "cliente_id")
private Cliente cliente;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "produto_id")
private ProdutoInvestimento produto;
```
Esses objetos só são carregados quando realmente necessários, reduzindo I/O e uso de memória em listagens e consultas recorrentes.

- Pipeline Enxuto de Recomendação
- O pipeline roda 100% em memória:
- Carrega regras e produtos (via repositórios cacheados).
- Filtra produtos inativos ou incompatíveis com o perfil do cliente.
- Calcula o perfil de risco e aplica preferências do cliente.
- Atribui um score de adequação com base em:
**risco, liquidez x prazo, rentabilidade, histórico de simulações/contratações.**

Ordena os produtos pelos melhores scores e retorna apenas os mais relevantes.

Todo esse processamento acontece na camada de aplicação, minimizando acessos ao banco e mantendo a API preparada para alto volume de requisições com baixa latência.


## Contato
Nome: Vinicius Pereira D'Amico

E-mail: vinicius.amico@caixa.gov.br

Trilha: Dev Back-end Java | Nível II (mas fiz as aulas do nível III)

Matrícula: C159473-4
