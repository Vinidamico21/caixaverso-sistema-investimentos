------------------------------------------------------------
-- V1__create_tables.sql
------------------------------------------------------------

------------------------------------------------------------
-- Tabela: Cliente
------------------------------------------------------------
CREATE TABLE dbo.Cliente (
    id                BIGINT IDENTITY(1,1) NOT NULL,
    nome              VARCHAR(150)         NOT NULL,
    documento         VARCHAR(20)          NOT NULL,
    email             VARCHAR(150)         NULL,
    renda_mensal      DECIMAL(18,2)        NULL,
    data_nascimento   DATE                 NULL,
    data_criacao      DATETIME2(0)         NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT PK_Cliente PRIMARY KEY (id),
    CONSTRAINT UQ_Cliente_Documento UNIQUE (documento),
    CONSTRAINT CK_Cliente_Renda_Nao_Negativa
        CHECK (renda_mensal IS NULL OR renda_mensal >= 0)
);


------------------------------------------------------------
-- Tabela: ProdutoInvestimento
------------------------------------------------------------
CREATE TABLE dbo.ProdutoInvestimento (
    id                BIGINT IDENTITY(1,1) NOT NULL,
    codigo            VARCHAR(50)          NOT NULL,
    nome              VARCHAR(150)         NOT NULL,
    tipo              VARCHAR(30)          NOT NULL,
    risco             VARCHAR(20)          NOT NULL,
    taxa_anual        DECIMAL(7,4)         NOT NULL,
    liquidez          VARCHAR(30)          NOT NULL,
    prazo_min_meses   INT                  NULL,
    prazo_max_meses   INT                  NULL,
    valor_minimo      DECIMAL(18,2)        NOT NULL,
    valor_maximo      DECIMAL(18,2)        NULL,
    ativo             BIT                  NOT NULL DEFAULT 1,
    data_criacao      DATETIME2(0)         NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT PK_ProdutoInvestimento PRIMARY KEY (id),
    CONSTRAINT UQ_ProdutoInvestimento_Codigo UNIQUE (codigo),
    CONSTRAINT CK_ProdutoInvestimento_Taxa_Nao_Negativa CHECK (taxa_anual >= 0),
    CONSTRAINT CK_ProdutoInvestimento_Valores_Nao_Negativos CHECK (
        valor_minimo >= 0 AND (valor_maximo IS NULL OR valor_maximo >= 0)
    ),
    CONSTRAINT CK_ProdutoInvestimento_Prazos_Nao_Negativos CHECK (
        (prazo_min_meses IS NULL OR prazo_min_meses >= 0) AND
        (prazo_max_meses IS NULL OR prazo_max_meses >= 0)
    ),
    CONSTRAINT CK_ProdutoInvestimento_Risco CHECK (
        risco IN ('CONSERVADOR', 'MODERADO', 'AGRESSIVO')
    )
);


------------------------------------------------------------
-- Tabela: SimulacaoInvestimento
------------------------------------------------------------
CREATE TABLE dbo.SimulacaoInvestimento (
    id                       BIGINT IDENTITY(1,1) NOT NULL,
    cliente_id               BIGINT               NOT NULL,
    produto_id               BIGINT               NOT NULL,
    valor_aplicado           DECIMAL(18,2)        NOT NULL,
    prazo_meses              INT                  NOT NULL,
    valor_final              DECIMAL(18,2)        NOT NULL,
    data_simulacao           DATETIME2(0)         NOT NULL DEFAULT SYSUTCDATETIME(),
    data_simulacao_dia       AS CAST(data_simulacao AS DATE) PERSISTED,
    perfil_risco_calculado   VARCHAR(20)          NULL,
    score_risco              DECIMAL(7,4)         NULL,
    data_criacao             DATETIME2(0)         NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT PK_SimulacaoInvestimento PRIMARY KEY (id),
    CONSTRAINT CK_Simulacao_Valor_Nao_Negativo CHECK (valor_aplicado >= 0),
    CONSTRAINT CK_Simulacao_ValorFinal_Nao_Negativo CHECK (valor_final >= 0),
    CONSTRAINT CK_Simulacao_Prazo_Nao_Negativo CHECK (prazo_meses >= 0),
    CONSTRAINT CK_Simulacao_Perfil_Risco CHECK (
        perfil_risco_calculado IS NULL OR
        perfil_risco_calculado IN ('CONSERVADOR', 'MODERADO', 'AGRESSIVO')
    )
);


------------------------------------------------------------
-- Tabela: Investimento
------------------------------------------------------------
CREATE TABLE dbo.Investimento (
    id                BIGINT IDENTITY(1,1) NOT NULL,
    cliente_id        BIGINT               NOT NULL,
    produto_id        BIGINT               NOT NULL,
    valor_aplicado    DECIMAL(18,2)        NOT NULL,
    prazo_meses       INT                  NULL,
    data_aporte       DATETIME2(0)         NOT NULL,
    status            VARCHAR(20)          NOT NULL,
    data_criacao      DATETIME2(0)         NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT PK_Investimento PRIMARY KEY (id),
    CONSTRAINT CK_Investimento_Valor_Aplicado CHECK (valor_aplicado >= 0),
    CONSTRAINT CK_Investimento_Status CHECK (
        status IN ('ABERTO', 'RESGATADO', 'CANCELADO')
    )
);


------------------------------------------------------------
-- Tabela: TelemetriaRegistro
------------------------------------------------------------
CREATE TABLE dbo.TelemetriaRegistro (
    id             BIGINT IDENTITY(1,1) NOT NULL,
    endpoint       VARCHAR(150)         NOT NULL,
    metodo_http    VARCHAR(10)          NOT NULL,
    sucesso        BIT                  NOT NULL,
    status_http    INT                  NULL,
    duracao_ms     INT                  NULL,
    data_registro  DATETIME2(0)         NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT PK_TelemetriaRegistro PRIMARY KEY (id)
);

CREATE INDEX IX_Telemetria_Endpoint_Data
    ON dbo.TelemetriaRegistro (endpoint, data_registro);


------------------------------------------------------------
-- FOREIGN KEYS
------------------------------------------------------------
ALTER TABLE dbo.SimulacaoInvestimento
    ADD CONSTRAINT FK_Simulacao_Cliente
    FOREIGN KEY (cliente_id) REFERENCES dbo.Cliente(id);

ALTER TABLE dbo.SimulacaoInvestimento
    ADD CONSTRAINT FK_Simulacao_Produto
    FOREIGN KEY (produto_id) REFERENCES dbo.ProdutoInvestimento(id);

ALTER TABLE dbo.Investimento
    ADD CONSTRAINT FK_Investimento_Cliente
    FOREIGN KEY (cliente_id) REFERENCES dbo.Cliente(id);

ALTER TABLE dbo.Investimento
    ADD CONSTRAINT FK_Investimento_Produto
    FOREIGN KEY (produto_id) REFERENCES dbo.ProdutoInvestimento(id);


------------------------------------------------------------
-- Tabela: ProdutoRiscoRegra
------------------------------------------------------------
CREATE TABLE dbo.ProdutoRiscoRegra (
    id INT IDENTITY(1,1) PRIMARY KEY,
    rentabilidade_min DECIMAL(10,4) NOT NULL,
    rentabilidade_max DECIMAL(10,4) NULL,
    risco VARCHAR(20) NOT NULL
);


------------------------------------------------------------
-- ÍNDICES ADICIONADOS PARA OTIMIZAÇÃO DE PERFORMANCE
------------------------------------------------------------

------------------------------------------------------------
-- CLIENTE
------------------------------------------------------------
CREATE INDEX IX_Cliente_Nome
    ON dbo.Cliente (nome);

------------------------------------------------------------
-- PRODUTOINVESTIMENTO
------------------------------------------------------------
CREATE INDEX IX_ProdutoInvestimento_Tipo
    ON dbo.ProdutoInvestimento (tipo);

CREATE INDEX IX_ProdutoInvestimento_Risco
    ON dbo.ProdutoInvestimento (risco);

CREATE INDEX IX_ProdutoInvestimento_Ativo
    ON dbo.ProdutoInvestimento (ativo);

CREATE INDEX IX_ProdutoInvestimento_Risco_Ativo
    ON dbo.ProdutoInvestimento (risco, ativo);

------------------------------------------------------------
-- SIMULACAO INVESTIMENTO
------------------------------------------------------------
CREATE INDEX IX_Simulacao_Cliente
    ON dbo.SimulacaoInvestimento (cliente_id);

CREATE INDEX IX_Simulacao_Produto
    ON dbo.SimulacaoInvestimento (produto_id);

CREATE INDEX IX_Simulacao_Data
    ON dbo.SimulacaoInvestimento (data_simulacao_dia);

CREATE INDEX IX_Simulacao_Cliente_Data
    ON dbo.SimulacaoInvestimento (cliente_id, data_simulacao_dia);

------------------------------------------------------------
-- INVESTIMENTO
------------------------------------------------------------
CREATE INDEX IX_Investimento_Cliente
    ON dbo.Investimento (cliente_id);

CREATE INDEX IX_Investimento_Produto
    ON dbo.Investimento (produto_id);

CREATE INDEX IX_Investimento_Status
    ON dbo.Investimento (status);

CREATE INDEX IX_Investimento_Cliente_Status
    ON dbo.Investimento (cliente_id, status);

------------------------------------------------------------
-- TELEMETRIA
------------------------------------------------------------
CREATE INDEX IX_Telemetria_Sucesso
    ON dbo.TelemetriaRegistro (sucesso);

CREATE INDEX IX_Telemetria_StatusHttp
    ON dbo.TelemetriaRegistro (status_http);

------------------------------------------------------------
-- PRODUTORISCOREGRA
------------------------------------------------------------
CREATE INDEX IX_ProdutoRisco_Range
    ON dbo.ProdutoRiscoRegra (rentabilidade_min, rentabilidade_max);
