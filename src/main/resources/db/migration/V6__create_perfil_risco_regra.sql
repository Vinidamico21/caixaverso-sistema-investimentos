-- =============================================================
-- Tabela de Regras de Classificação de Perfil de Risco
-- =============================================================
CREATE TABLE PerfilRiscoRegra (
    id INT IDENTITY(1,1) PRIMARY KEY,
    score_min INT NOT NULL,
    score_max INT NOT NULL,
    perfil VARCHAR(20) NOT NULL
);

-- =============================================================
-- Dados iniciais (mockados / configuráveis futuramente)
-- =============================================================
INSERT INTO PerfilRiscoRegra (score_min, score_max, perfil) VALUES
(0, 40, 'CONSERVADOR'),
(41, 70, 'MODERADO'),
(71, 100, 'AGRESSIVO');
