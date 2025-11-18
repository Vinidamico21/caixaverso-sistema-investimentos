------------------------------------------------------------
-- V3__insert_clientes_investimentos_simulacoes.sql  (ATUALIZADO)
-- Carga inicial de Clientes, Investimentos e Simulações
------------------------------------------------------------


------------------------------------------------------------
-- CLIENTES DE TESTE
------------------------------------------------------------
INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'João Silva', '11111111111', 'joao@teste.com', 3000.00, '1990-05-12'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='11111111111');

INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Maria Santos', '22222222222', 'maria@teste.com', 12000.00, '1985-03-20'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='22222222222');

INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Pedro Lima', '33333333333', 'pedro@teste.com', 8000.00, '1995-11-08'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='33333333333');

INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Ana Costa', '44444444444', 'ana@teste.com', 15000.00, '1988-07-01'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='44444444444');

INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Lucas Pereira', '55555555555', 'lucas@teste.com', 5000.00, '1992-09-15'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='55555555555');

INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Thiago Ribeiro', '15151515151', 'thiago@teste.com', 25000.00, '1988-07-20'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='15151515151');

INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Mariana Silva', '16161616161', 'mariana@teste.com', 32000.00, '1990-11-11'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='16161616161');

INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Diego Campos', '17171717171', 'diego@teste.com', 28000.00, '1984-03-02'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='17171717171');

------------------------------------------------------------
-- INVESTIMENTOS REAIS - CLIENTES 1 A 3
------------------------------------------------------------

-- Cliente 1 (João) – Conservador
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='11111111111'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029'),
    1000.00, 12, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='11111111111')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029')
);

INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='11111111111'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_LIQ_DIARIA_100_CDI'),
    2000.00, 6, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='11111111111')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_LIQ_DIARIA_100_CDI')
);


-- Cliente 2 (Maria) – Moderado
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='22222222222'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS'),
    7000.00, 24, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='22222222222')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS')
);

INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='22222222222'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X'),
    5000.00, 18, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='22222222222')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X')
);


-- Cliente 3 (Pedro) – Agressivo
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='33333333333'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4'),
    15000.00, 36, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='33333333333')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4')
);




------------------------------------------------------------
-- SIMULAÇÕES – CLIENTES 1 A 3
------------------------------------------------------------

-- Cliente 1 (João – Conservador)
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='11111111111'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029'),
    1000.00, 12, 1120.00,
    'CONSERVADOR', 0.25
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='11111111111')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029')
      AND valor_aplicado=1000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='11111111111'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_LIQ_DIARIA_100_CDI'),
    2000.00, 6, 2127.00,
    'CONSERVADOR', 0.30
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='11111111111')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_LIQ_DIARIA_100_CDI')
      AND valor_aplicado=2000.00
);



-- Cliente 2 (Maria – Moderado)
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='22222222222'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS'),
    7000.00, 12, 7500.00,
    'MODERADO', 0.55
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='22222222222')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS')
      AND valor_aplicado=7000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='22222222222'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X'),
    5000.00, 18, 5800.00,
    'MODERADO', 0.60
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='22222222222')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X')
      AND valor_aplicado=5000.00
);


-- Cliente 3 (Pedro – Agressivo)
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='33333333333'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4'),
    15000.00, 36, 18750.00,
    'AGRESSIVO', 0.80
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='33333333333')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4')
      AND valor_aplicado=15000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='33333333333'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X'),
    9000.00, 24, 11000.00,
    'AGRESSIVO', 0.75
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='33333333333')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X')
      AND valor_aplicado=9000.00
);


