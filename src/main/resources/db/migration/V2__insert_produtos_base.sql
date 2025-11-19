------------------------------------------------------------
-- V2__insert_produtos_parametros_pesos.sql  (ATUALIZADO)
-- Carga inicial de produtos, perfis fixos, parâmetros de risco e pesos
------------------------------------------------------------

-- TESOURO DIRETO
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'TESOURO_IPCA_2035', 'Tesouro IPCA+ 2035', 'TESOURO', 'CONSERVADOR',
       0.0550, 'NO_VENCIMENTO', 24, 144, 35.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'TESOURO_IPCA_2035');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'TESOURO_PREFIX_2026', 'Tesouro Prefixado 2026', 'TESOURO', 'MODERADO',
       0.1180, 'NO_VENCIMENTO', 12, 24, 30.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'TESOURO_PREFIX_2026');

-- CDBs
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'CDB_110_CDI_2ANOS', 'CDB 110% do CDI 2 anos', 'CDB', 'CONSERVADOR',
       0.1400, 'NO_VENCIMENTO', 24, 24, 1000.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'CDB_110_CDI_2ANOS');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'CDB_BANCO_X_95_CDI', 'CDB Banco X 95% do CDI', 'CDB', 'CONSERVADOR',
       0.1210, 'DIARIA', 1, 36, 500.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'CDB_BANCO_X_95_CDI');

-- LCIs e LCAs
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'LCA_AGRO_2_ANOS', 'LCA Agro 2 anos', 'LCA', 'MODERADO',
       0.1280, 'NO_VENCIMENTO', 24, 24, 1000.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'LCA_AGRO_2_ANOS');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'LCI_CONST_CIVIL_4ANOS', 'LCI Construção Civil 4 anos', 'LCI', 'MODERADO',
       0.1420, 'NO_VENCIMENTO', 36, 48, 2000.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'LCI_CONST_CIVIL_4ANOS');

-- FUNDOS DE INVESTIMENTO
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'FUNDO_RENDA_FIXA_Y', 'Fundo Renda Fixa Y', 'FUNDO', 'CONSERVADOR',
       0.1150, 'D+1', 0, NULL, 100.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'FUNDO_RENDA_FIXA_Y');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'FUNDO_ACOES_BRASIL', 'Fundo Ações Brasil', 'FUNDO', 'AGRESSIVO',
       0.1850, 'D+5', 0, NULL, 200.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'FUNDO_ACOES_BRASIL');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'FUNDO_EXTERIOR_USA', 'Fundo Exterior EUA', 'FUNDO', 'MODERADO',
       0.1250, 'D+7', 0, NULL, 1000.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'FUNDO_EXTERIOR_USA');

-- AÇÕES
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'ACAO_VALE3', 'Ação VALE3', 'ACAO', 'AGRESSIVO',
       0.1800, 'DIARIA', 0, NULL, 60.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'ACAO_VALE3');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'ACAO_ITUB4', 'Ação ITUB4', 'ACAO', 'MODERADO',
       0.1200, 'DIARIA', 0, NULL, 40.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'ACAO_ITUB4');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'ACAO_BBDC4', 'Ação BBDC4', 'ACAO', 'MODERADO',
       0.1350, 'DIARIA', 0, NULL, 35.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'ACAO_BBDC4');

-- FIIS (FUNDOS IMOBILIÁRIOS)
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'FII_HGLG11', 'FII HGLG11', 'FII', 'MODERADO',
       0.0950, 'DIARIA', 0, NULL, 80.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'FII_HGLG11');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'FII_KNRI11', 'FII KNRI11', 'FII', 'MODERADO',
       0.1050, 'DIARIA', 0, NULL, 90.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'FII_KNRI11');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'FII_MXRF11', 'FII MXRF11', 'FII', 'CONSERVADOR',
       0.0850, 'DIARIA', 0, NULL, 70.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'FII_MXRF11');

-- DEBÊNTURES
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'DEBENT_ENERG_2028', 'Debênture Energia 2028', 'DEBENTURE', 'MODERADO',
       0.1520, 'NO_VENCIMENTO', 36, 60, 5000.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'DEBENT_ENERG_2028');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'DEBENT_INFRA_2030', 'Debênture Infraestrutura 2030', 'DEBENTURE', 'MODERADO',
       0.1480, 'NO_VENCIMENTO', 48, 84, 3000.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'DEBENT_INFRA_2030');

-- PREVIDÊNCIA PRIVADA
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'PREV_PGBL_CONSERV', 'PGBL Perfil Conservador', 'PREVIDENCIA', 'CONSERVADOR',
       0.0980, 'D+30', 60, 480, 100.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'PREV_PGBL_CONSERV');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'PREV_VGBL_MODERADO', 'VGBL Perfil Moderado', 'PREVIDENCIA', 'MODERADO',
       0.1250, 'D+30', 60, 480, 100.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'PREV_VGBL_MODERADO');

-- COE (CERTIFICADO DE OPERAÇÕES ESTRUTURADAS)
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'COE_PROTECAO_2ANOS', 'COE com Proteção 2 anos', 'COE', 'MODERADO',
       0.1450, 'NO_VENCIMENTO', 24, 24, 2000.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'COE_PROTECAO_2ANOS');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'COE_ALAVANCADO_3ANOS', 'COE Alavancado 3 anos', 'COE', 'AGRESSIVO',
       0.2200, 'NO_VENCIMENTO', 36, 36, 5000.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'COE_ALAVANCADO_3ANOS');

-- ETF (EXCHANGE TRADED FUNDS)
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'ETF_BOVA11', 'ETF BOVA11', 'ETF', 'MODERADO',
       0.1100, 'DIARIA', 0, NULL, 85.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'ETF_BOVA11');

INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'ETF_SMAL11', 'ETF SMAL11', 'ETF', 'AGRESSIVO',
       0.1550, 'DIARIA', 0, NULL, 75.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'ETF_SMAL11');

-- TESOURO SELIC 2029 (Conservador)
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'TESOURO_SELIC_2029', 'Tesouro Selic 2029', 'TESOURO', 'CONSERVADOR',
       0.0910, 'DIARIA', 1, 120, 35.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'TESOURO_SELIC_2029');


-- CDB Liquidez Diária 100% CDI
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'CDB_LIQ_DIARIA_100_CDI', 'CDB 100% CDI Liquidez Diária', 'CDB', 'CONSERVADOR',
       0.1180, 'DIARIA', 1, 36, 100.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'CDB_LIQ_DIARIA_100_CDI');


-- LCI Imobiliária 3 anos
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'LCI_IMOB_3_ANOS', 'LCI Imobiliária 3 anos', 'LCI', 'CONSERVADOR',
       0.1050, 'NO_VENCIMENTO', 36, 36, 1000.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'LCI_IMOB_3_ANOS');


-- Fundo Multimercado X
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'FUND_MULTIMERCADO_X', 'Fundo Multimercado X', 'FUNDO', 'MODERADO',
       0.1450, 'D+15', 0, NULL, 500.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'FUND_MULTIMERCADO_X');


-- Ação PETR4
INSERT INTO dbo.ProdutoInvestimento
(codigo, nome, tipo, risco, taxa_anual, liquidez, prazo_min_meses, prazo_max_meses,
 valor_minimo, valor_maximo, ativo)
SELECT 'ACAO_PETR4', 'Ação PETR4', 'ACAO', 'AGRESSIVO',
       0.1650, 'DIARIA', 0, NULL, 30.00, NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.ProdutoInvestimento WHERE codigo = 'ACAO_PETR4');

------------------------------------------------------------
-- Insert Tabela Produto_risco_regra
------------------------------------------------------------
INSERT INTO dbo.ProdutoRiscoRegra (rentabilidade_min, rentabilidade_max, risco) VALUES
(0.00, 0.10, 'BAIXO'),
(0.10, 0.15, 'MEDIO'),
(0.15, NULL, 'ALTO');

