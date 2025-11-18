------------------------------------------------------------
-- V5__insert_maior_volume_simulacoes.sql
-- Criação de maior volume de simulações para todos os clientes
------------------------------------------------------------

-- Cliente 1 (João - Conservador) - Mais 5 simulações
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='11111111111'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_IPCA_2035'),
    1500.00, 24, 1650.00,
    'CONSERVADOR', 0.28
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='11111111111')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_IPCA_2035')
      AND valor_aplicado=1500.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='11111111111'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_BANCO_X_95_CDI'),
    3000.00, 12, 3360.00,
    'CONSERVADOR', 0.32
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='11111111111')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_BANCO_X_95_CDI')
      AND valor_aplicado=3000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='11111111111'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUNDO_RENDA_FIXA_Y'),
    2000.00, 18, 2230.00,
    'CONSERVADOR', 0.35
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='11111111111')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUNDO_RENDA_FIXA_Y')
      AND valor_aplicado=2000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='11111111111'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FII_MXRF11'),
    1000.00, 36, 1280.00,
    'CONSERVADOR', 0.38
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='11111111111')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FII_MXRF11')
      AND valor_aplicado=1000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='11111111111'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='PREV_PGBL_CONSERV'),
    5000.00, 120, 12900.00,
    'CONSERVADOR', 0.25
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='11111111111')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='PREV_PGBL_CONSERV')
      AND valor_aplicado=5000.00
);

-- Cliente 2 (Maria - Moderado) - Mais 6 simulações
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='22222222222'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_PREFIX_2026'),
    8000.00, 18, 9440.00,
    'MODERADO', 0.58
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='22222222222')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_PREFIX_2026')
      AND valor_aplicado=8000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='22222222222'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCA_AGRO_2_ANOS'),
    10000.00, 24, 12870.00,
    'MODERADO', 0.52
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='22222222222')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCA_AGRO_2_ANOS')
      AND valor_aplicado=10000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='22222222222'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUNDO_EXTERIOR_USA'),
    15000.00, 36, 21300.00,
    'MODERADO', 0.62
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='22222222222')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUNDO_EXTERIOR_USA')
      AND valor_aplicado=15000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='22222222222'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_ITUB4'),
    6000.00, 24, 7600.00,
    'MODERADO', 0.65
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='22222222222')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_ITUB4')
      AND valor_aplicado=6000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='22222222222'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FII_KNRI11'),
    4000.00, 48, 5800.00,
    'MODERADO', 0.55
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='22222222222')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FII_KNRI11')
      AND valor_aplicado=4000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='22222222222'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='PREV_VGBL_MODERADO'),
    20000.00, 240, 362000.00,
    'MODERADO', 0.60
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='22222222222')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='PREV_VGBL_MODERADO')
      AND valor_aplicado=20000.00
);

-- Cliente 3 (Pedro - Agressivo) - Mais 7 simulações
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='33333333333'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_VALE3'),
    25000.00, 24, 35500.00,
    'AGRESSIVO', 0.82
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='33333333333')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_VALE3')
      AND valor_aplicado=25000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='33333333333'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_BBDC4'),
    18000.00, 18, 22900.00,
    'AGRESSIVO', 0.78
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='33333333333')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_BBDC4')
      AND valor_aplicado=18000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='33333333333'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUNDO_ACOES_BRASIL'),
    12000.00, 24, 17100.00,
    'AGRESSIVO', 0.75
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='33333333333')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUNDO_ACOES_BRASIL')
      AND valor_aplicado=12000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='33333333333'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ETF_BOVA11'),
    8000.00, 36, 10600.00,
    'AGRESSIVO', 0.72
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='33333333333')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ETF_BOVA11')
      AND valor_aplicado=8000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='33333333333'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ETF_SMAL11'),
    6000.00, 24, 8400.00,
    'AGRESSIVO', 0.85
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='33333333333')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ETF_SMAL11')
      AND valor_aplicado=6000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='33333333333'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='COE_ALAVANCADO_3ANOS'),
    15000.00, 36, 24300.00,
    'AGRESSIVO', 0.88
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='33333333333')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='COE_ALAVANCADO_3ANOS')
      AND valor_aplicado=15000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='33333333333'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='DEBENT_ENERG_2028'),
    10000.00, 48, 17600.00,
    'AGRESSIVO', 0.70
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='33333333333')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='DEBENT_ENERG_2028')
      AND valor_aplicado=10000.00
);

------------------------------------------------------------
-- SIMULAÇÕES ADICIONAIS PARA CLIENTES 4 A 10
------------------------------------------------------------

-- Cliente 4 (Carlos - Conservador) - Mais 4 simulações
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='66666666666'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_110_CDI_2ANOS'),
    2000.00, 24, 2560.00,
    'CONSERVADOR', 0.30
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='66666666666')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_110_CDI_2ANOS')
      AND valor_aplicado=2000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='66666666666'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FII_HGLG11'),
    1200.00, 24, 1450.00,
    'CONSERVADOR', 0.35
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='66666666666')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FII_HGLG11')
      AND valor_aplicado=1200.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='66666666666'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_CONST_CIVIL_4ANOS'),
    3000.00, 48, 4800.00,
    'CONSERVADOR', 0.32
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='66666666666')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCI_CONST_CIVIL_4ANOS')
      AND valor_aplicado=3000.00
);

-- Cliente 5 (Julia - Moderado) - Mais 5 simulações
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='77777777777'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='DEBENT_INFRA_2030'),
    8000.00, 60, 15800.00,
    'MODERADO', 0.58
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='77777777777')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='DEBENT_INFRA_2030')
      AND valor_aplicado=8000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='77777777777'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='COE_PROTECAO_2ANOS'),
    5000.00, 24, 6700.00,
    'MODERADO', 0.55
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='77777777777')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='COE_PROTECAO_2ANOS')
      AND valor_aplicado=5000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='77777777777'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_BBDC4'),
    3000.00, 12, 3400.00,
    'MODERADO', 0.62
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='77777777777')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_BBDC4')
      AND valor_aplicado=3000.00
);

-- Cliente 6 (Ricardo - Agressivo) - Mais 6 simulações
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='88888888888'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ETF_SMAL11'),
    15000.00, 36, 23100.00,
    'AGRESSIVO', 0.82
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='88888888888')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ETF_SMAL11')
      AND valor_aplicado=15000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='88888888888'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='COE_ALAVANCADO_3ANOS'),
    25000.00, 36, 40500.00,
    'AGRESSIVO', 0.88
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='88888888888')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='COE_ALAVANCADO_3ANOS')
      AND valor_aplicado=25000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='88888888888'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_VALE3'),
    18000.00, 24, 25500.00,
    'AGRESSIVO', 0.80
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='88888888888')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_VALE3')
      AND valor_aplicado=18000.00
);

-- Cliente 7 (Patricia - Conservador) - Mais 3 simulações
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='99999999999'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_BANCO_X_95_CDI'),
    1000.00, 6, 1060.00,
    'CONSERVADOR', 0.20
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='99999999999')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='CDB_BANCO_X_95_CDI')
      AND valor_aplicado=1000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='99999999999'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUNDO_RENDA_FIXA_Y'),
    1500.00, 12, 1670.00,
    'CONSERVADOR', 0.25
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='99999999999')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUNDO_RENDA_FIXA_Y')
      AND valor_aplicado=1500.00
);

-- Cliente 8 (Gabriel - Moderado) - Mais 4 simulações
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='12121212121'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCA_AGRO_2_ANOS'),
    7000.00, 24, 9000.00,
    'MODERADO', 0.56
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='12121212121')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='LCA_AGRO_2_ANOS')
      AND valor_aplicado=7000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='12121212121'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ETF_BOVA11'),
    4000.00, 18, 4760.00,
    'MODERADO', 0.60
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='12121212121')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ETF_BOVA11')
      AND valor_aplicado=4000.00
);

-- Cliente 9 (Leticia - Agressivo) - Mais 5 simulações
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='13131313131'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUNDO_ACOES_BRASIL'),
    15000.00, 24, 21300.00,
    'AGRESSIVO', 0.78
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='13131313131')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FUNDO_ACOES_BRASIL')
      AND valor_aplicado=15000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='13131313131'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='COE_ALAVANCADO_3ANOS'),
    20000.00, 36, 32400.00,
    'AGRESSIVO', 0.85
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='13131313131')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='COE_ALAVANCADO_3ANOS')
      AND valor_aplicado=20000.00
);

-- Cliente 10 (Fernando - Moderado) - Mais 3 simulações
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='14141414141'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_PREFIX_2026'),
    3000.00, 18, 3540.00,
    'MODERADO', 0.54
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='14141414141')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_PREFIX_2026')
      AND valor_aplicado=3000.00
);

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='14141414141'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FII_HGLG11'),
    2000.00, 36, 2580.00,
    'MODERADO', 0.58
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='14141414141')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='FII_HGLG11')
      AND valor_aplicado=2000.00
);

------------------------------------------------------------
-- SIMULAÇÕES COM DIFERENTES PERFIS PARA TESTE DO MOTOR
-- (Algumas simulações com perfis diferentes do perfil principal do cliente)
------------------------------------------------------------

-- Cliente 1 tentando produtos moderados
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='11111111111'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_ITUB4'),
    500.00, 6, 530.00,
    'MODERADO', 0.45
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='11111111111')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_ITUB4')
      AND valor_aplicado=500.00
);

-- Cliente 3 tentando produtos conservadores
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='33333333333'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029'),
    3000.00, 6, 3150.00,
    'CONSERVADOR', 0.35
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='33333333333')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='TESOURO_SELIC_2029')
      AND valor_aplicado=3000.00
);

-- Cliente 5 tentando produtos agressivos
INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final,
                                       perfil_risco_calculado, score_risco)
SELECT
    (SELECT id FROM dbo.Cliente WHERE documento='77777777777'),
    (SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4'),
    2000.00, 12, 2500.00,
    'AGRESSIVO', 0.68
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.SimulacaoInvestimento
    WHERE cliente_id=(SELECT id FROM dbo.Cliente WHERE documento='77777777777')
      AND produto_id=(SELECT id FROM dbo.ProdutoInvestimento WHERE codigo='ACAO_PETR4')
      AND valor_aplicado=2000.00
);

DECLARE @cli11 BIGINT = (SELECT id FROM Cliente WHERE documento='15151515151');

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final, perfil_risco_calculado, score_risco)
SELECT @cli11, id, 2000, 12, 2500, 'AGRESSIVO', 0.80
FROM ProdutoInvestimento
WHERE codigo IN ('ACAO_PETR4','ACAO_VALE3','ETF_SMAL11','FUND_MULTIMERCADO_X');

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final, perfil_risco_calculado, score_risco)
SELECT @cli11, id, 2500, 12, 3100, 'AGRESSIVO', 0.82
FROM ProdutoInvestimento
WHERE codigo IN ('ACAO_PETR4','ACAO_VALE3');

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final, perfil_risco_calculado, score_risco)
SELECT @cli11, id, 1500, 6, 1650, 'MODERADO', 0.55
FROM ProdutoInvestimento
WHERE codigo IN ('CDB_BANCO_X_95_CDI','ETF_BOVA11');

DECLARE @cli12 BIGINT = (SELECT id FROM Cliente WHERE documento='16161616161');

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final, perfil_risco_calculado, score_risco)
SELECT @cli12, id, 3000, 12, 3800, 'AGRESSIVO', 0.85
FROM ProdutoInvestimento
WHERE codigo IN ('ACAO_PETR4','ETF_SMAL11','COE_ALAVANCADO_3ANOS','FUND_MULTIMERCADO_X');

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final, perfil_risco_calculado, score_risco)
SELECT @cli12, id, 3500, 12, 4200, 'AGRESSIVO', 0.88
FROM ProdutoInvestimento
WHERE codigo IN ('ACAO_PETR4','ETF_SMAL11');

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final, perfil_risco_calculado, score_risco)
SELECT @cli12, id, 2000, 6, 2250, 'MODERADO', 0.50
FROM ProdutoInvestimento
WHERE codigo IN ('LCI_IMOB_3_ANOS','CDB_LIQ_DIARIA_100_CDI');

DECLARE @cli13 BIGINT = (SELECT id FROM Cliente WHERE documento='17171717171');

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final, perfil_risco_calculado, score_risco)
SELECT @cli13, id, 2500, 12, 3100, 'AGRESSIVO', 0.81
FROM ProdutoInvestimento
WHERE codigo IN ('ACAO_VALE3','ACAO_PETR4','ETF_SMAL11','FUND_MULTIMERCADO_X');

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final, perfil_risco_calculado, score_risco)
SELECT @cli13, id, 2700, 12, 3400, 'AGRESSIVO', 0.83
FROM ProdutoInvestimento
WHERE codigo IN ('ACAO_VALE3','ETF_SMAL11');

INSERT INTO dbo.SimulacaoInvestimento (cliente_id, produto_id, valor_aplicado, prazo_meses, valor_final, perfil_risco_calculado, score_risco)
SELECT @cli13, id, 1500, 6, 1650, 'MODERADO', 0.52
FROM ProdutoInvestimento
WHERE codigo IN ('CDB_BANCO_X_95_CDI','ETF_BOVA11');