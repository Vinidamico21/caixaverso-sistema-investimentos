---------------------------------------------------------
-- TABELA 1 — PREFERÊNCIA (Liquidez x Rentabilidade)
---------------------------------------------------------
CREATE TABLE perfil_preferencia_regra (
    id INT IDENTITY(1,1) PRIMARY KEY,
    tipo_preferencia VARCHAR(20) NOT NULL,  -- LIQUIDEZ / RENTABILIDADE / EMPATE
    pontuacao INT NOT NULL                  -- 0 a 50
);

INSERT INTO perfil_preferencia_regra (tipo_preferencia, pontuacao) VALUES
('RENTABILIDADE', 50),   -- Preferência forte por rentabilidade → Agressivo
('EMPATE',        30),   -- Igual → Moderado
('LIQUIDEZ',      10);   -- Preferência por liquidez → Conservador


---------------------------------------------------------
-- TABELA 2 — FREQUÊNCIA: Investimentos reais
---------------------------------------------------------
CREATE TABLE perfil_freq_invest_regra (
    id INT IDENTITY(1,1) PRIMARY KEY,
    quantidade_min INT NOT NULL,
    quantidade_max INT NULL, -- NULL = ilimitado
    pontuacao INT NOT NULL    -- 0..14
);

INSERT INTO perfil_freq_invest_regra (quantidade_min, quantidade_max, pontuacao) VALUES
(10, NULL, 14),
(7,  9,   12),
(5,  6,   10),
(3,  4,   8),
(2,  2,   6),
(1,  1,   4),
(0,  0,   2);


---------------------------------------------------------
-- TABELA 3 — FREQUÊNCIA: Simulações
---------------------------------------------------------
CREATE TABLE perfil_freq_simulacao_regra (
    id INT IDENTITY(1,1) PRIMARY KEY,
    quantidade_min INT NOT NULL,
    quantidade_max INT NULL,
    pontuacao INT NOT NULL  -- 0..6
);

INSERT INTO perfil_freq_simulacao_regra (quantidade_min, quantidade_max, pontuacao) VALUES
(15, NULL, 6),
(10, 14, 5),
(7,  9,  4),
(5,  6,  3),
(3,  4,  2),
(1,  2,  1),
(0,  0,  0);
