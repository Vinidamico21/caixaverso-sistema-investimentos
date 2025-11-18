------------------------------------------------------------
-- V4__insert_clientes_4_a_10.sql (ATUALIZADO)
-- Criação de clientes adicionais + investimentos + simulações
------------------------------------------------------------



------------------------------------------------------------
-- CLIENTES 4 a 10 (sem perfil_risco_id, sem data_atualizacao)
------------------------------------------------------------

-- Cliente 4 – Conservador
INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Carlos Mendes', '66666666666', 'carlos@teste.com', 2500.00, '1980-04-10'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='66666666666');

-- Cliente 5 – Moderado
INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Julia Fernandes', '77777777777', 'julia@teste.com', 9000.00, '1994-02-18'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='77777777777');

-- Cliente 6 – Agressivo
INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Ricardo Alves', '88888888888', 'ricardo@teste.com', 18000.00, '1983-08-05'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='88888888888');

-- Cliente 7 – Conservador
INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Patricia Moura', '99999999999', 'patricia@teste.com', 4000.00, '1991-06-12'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='99999999999');

-- Cliente 8 – Moderado
INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Gabriel Rocha', '12121212121', 'gabriel@teste.com', 11000.00, '1997-01-22'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='12121212121');

-- Cliente 9 – Agressivo
INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Leticia Souza', '13131313131', 'leticia@teste.com', 20000.00, '1989-03-09'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='13131313131');

-- Cliente 10 – Moderado
INSERT INTO dbo.Cliente (nome, documento, email, renda_mensal, data_nascimento)
SELECT 'Fernando Cruz', '14141414141', 'fernando@teste.com', 7000.00, '1993-12-01'
WHERE NOT EXISTS (SELECT 1 FROM dbo.Cliente WHERE documento='14141414141');



------------------------------------------------------------
-- INVESTIMENTOS – CLIENTES 4 A 10 (com structure atual)
------------------------------------------------------------

-- Cliente 4 – Conservador (Tesouro + CDB)
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM dbo.Cliente WHERE documento='66666666666'),
       (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029'),
       800.00, 6, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='66666666666')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029')
);

INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM dbo.Cliente WHERE documento='66666666666'),
       (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_LIQ_DIARIA_100_CDI'),
       1500.00, 4, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='66666666666')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_LIQ_DIARIA_100_CDI')
);



-- Cliente 5 – Moderado (LCI + Fundo)
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM dbo.Cliente WHERE documento='77777777777'),
       (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS'),
       6000.00, 24, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='77777777777')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS')
);

INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM dbo.Cliente WHERE documento='77777777777'),
       (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X'),
       4000.00, 18, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='77777777777')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X')
);



-- Cliente 6 – Agressivo (PETR4 + Fundo)
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM dbo.Cliente WHERE documento='88888888888'),
       (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4'),
       20000.00, 36, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='88888888888')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4')
);

INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM dbo.Cliente WHERE documento='88888888888'),
       (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X'),
       7000.00, 12, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='88888888888')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X')
);



-- Cliente 7 – Conservador
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM dbo.Cliente WHERE documento='99999999999'),
       (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029'),
       500.00, 3, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='99999999999')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029')
);



-- Cliente 8 – Moderado
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM dbo.Cliente WHERE documento='12121212121'),
       (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X'),
       6000.00, 12, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='12121212121')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X')
);



-- Cliente 9 – Agressivo
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM dbo.Cliente WHERE documento='13131313131'),
       (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4'),
       12000.00, 30, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='13131313131')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4')
);



-- Cliente 10 – Moderado
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM dbo.Cliente WHERE documento='14141414141'),
       (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS'),
       5000.00, 24, GETUTCDATE(), 'ABERTO'
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.Investimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='14141414141')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS')
);

------------------------------------------------------------
-- INVESTIMENTOS CLIENTE 11 (AGRESSIVO)
------------------------------------------------------------
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM Cliente WHERE documento='15151515151'),
       (SELECT id FROM ProdutoInvestimento WHERE codigo='ACAO_PETR4'),
       15000.00, 24, GETUTCDATE(), 'ABERTO';

INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM Cliente WHERE documento='15151515151'),
       (SELECT id FROM ProdutoInvestimento WHERE codigo='ACAO_VALE3'),
       12000.00, 18, GETUTCDATE(), 'ABERTO';

INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM Cliente WHERE documento='15151515151'),
       (SELECT id FROM ProdutoInvestimento WHERE codigo='ETF_SMAL11'),
       8000.00, 12, GETUTCDATE(), 'ABERTO';


------------------------------------------------------------
-- INVESTIMENTOS CLIENTE 12 (AGRESSIVO)
------------------------------------------------------------
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM Cliente WHERE documento='16161616161'),
       (SELECT id FROM ProdutoInvestimento WHERE codigo='COE_ALAVANCADO_3ANOS'),
       20000.00, 36, GETUTCDATE(), 'ABERTO';

INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM Cliente WHERE documento='16161616161'),
       (SELECT id FROM ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X'),
       9000.00, 12, GETUTCDATE(), 'ABERTO';

INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM Cliente WHERE documento='16161616161'),
       (SELECT id FROM ProdutoInvestimento WHERE codigo='ACAO_PETR4'),
       7000.00, 18, GETUTCDATE(), 'ABERTO';


------------------------------------------------------------
-- INVESTIMENTOS CLIENTE 13 (AGRESSIVO)
------------------------------------------------------------
INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM Cliente WHERE documento='17171717171'),
       (SELECT id FROM ProdutoInvestimento WHERE codigo='ACAO_VALE3'),
       18000.00, 24, GETUTCDATE(), 'ABERTO';

INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM Cliente WHERE documento='17171717171'),
       (SELECT id FROM ProdutoInvestimento WHERE codigo='ETF_SMAL11'),
       9000.00, 12, GETUTCDATE(), 'ABERTO';

INSERT INTO dbo.Investimento (cliente_id, produto_id, valor_aplicado, prazo_meses, data_aporte, status)
SELECT (SELECT id FROM Cliente WHERE documento='17171717171'),
       (SELECT id FROM ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X'),
       8000.00, 12, GETUTCDATE(), 'ABERTO';

------------------------------------------------------------
-- SIMULAÇÕES – CLIENTES 4 A 10
------------------------------------------------------------

-- Cliente 4 – Conservador
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='66666666666'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029'),
    800.00, 6, 840.00,
    'CONSERVADOR', 0.22
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='66666666666')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029')
      AND valor_aplicado=800.00
);



-- Cliente 5 – Moderado
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='77777777777'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS'),
    6000.00, 12, 6450.00,
    'MODERADO', 0.50
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='77777777777')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS')
      AND valor_aplicado=6000.00
);



-- Cliente 6 – Agressivo
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='88888888888'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4'),
    20000.00, 24, 26000.00,
    'AGRESSIVO', 0.85
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='88888888888')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4')
      AND valor_aplicado=20000.00
);



-- Cliente 7 – Conservador
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='99999999999'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029'),
    500.00, 3, 515.00,
    'CONSERVADOR', 0.18
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='99999999999')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029')
      AND valor_aplicado=500.00
);



-- Cliente 8 – Moderado
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='12121212121'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X'),
    6000.00, 12, 6900.00,
    'MODERADO', 0.58
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='12121212121')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUND_MULTIMERCADO_X')
      AND valor_aplicado=6000.00
);



-- Cliente 9 – Agressivo
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='13131313131'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4'),
    12000.00, 18, 15000.00,
    'AGRESSIVO', 0.78
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='13131313131')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4')
      AND valor_aplicado=12000.00
);



-- Cliente 10 – Moderado
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='14141414141'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS'),
    5000.00, 18, 5600.00,
    'MODERADO', 0.52
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='14141414141')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_IMOB_3_ANOS')
      AND valor_aplicado=5000.00
);
