package br.com.caixaverso.invest.domain.constants;

import java.math.BigDecimal;

public final class PerfilConstantes {

    private PerfilConstantes() {}

    // ===========================
    // PERFIS
    // ===========================
    public static final String PERFIL_CONSERVADOR = "CONSERVADOR";
    public static final String PERFIL_MODERADO = "MODERADO";
    public static final String PERFIL_AGRESSIVO = "AGRESSIVO";
    public static final String PERFIL_DESCONHECIDO = "DESCONHECIDO";

    // ===========================
    // PREFERÊNCIAS
    // ===========================
    public static final String PREF_LIQUIDEZ = "LIQUIDEZ";
    public static final String PREF_RENTABILIDADE = "RENTABILIDADE";
    public static final String PREF_EMPATE = "EMPATE";

    // ===========================
    // DESCRIÇÕES DO PERFIL
    // ===========================
    public static final String DESC_CONSERVADOR =
            "Busca segurança e liquidez.";

    public static final String DESC_MODERADO =
            "Equilíbrio entre liquidez e rentabilidade.";

    public static final String DESC_AGRESSIVO =
            "Busca alta rentabilidade, aceitando maior risco.";

    public static final String DESC_DESCONHECIDO =
            "Perfil não classificado.";

    // ===========================
    // STRINGS DE LIQUIDEZ
    // ===========================
    public static final String LIQ_DIARIA = "DIARIA";
    public static final String LIQ_D0 = "D+0";
    public static final String LIQ_MENSAL = "MENSAL";
    public static final String LIQ_D30 = "D+30";

    // ===========================
    // RENTABILIDADE (limiares)
    // ===========================
    public static final BigDecimal RENTABILIDADE_BAIXA = new BigDecimal("0.08");
    public static final BigDecimal RENTABILIDADE_MEDIA = new BigDecimal("0.12");

    // ===========================
    // PRAZOS
    // ===========================
    public static final int PRAZO_CURTO = 3;
    public static final int PRAZO_MEDIO = 12;

    // ===========================
    // SCORE BÁSICO (produtos)
    // ===========================
    public static final BigDecimal SCORE_BAIXO = BigDecimal.ONE;
    public static final BigDecimal SCORE_MEDIO = new BigDecimal("2");
    public static final BigDecimal SCORE_ALTO = new BigDecimal("3");

    // ===========================
    // PESOS - CONSERVADOR
    // ===========================
    public static final BigDecimal PESO_LIQ_CONSERVADOR = new BigDecimal("0.6");
    public static final BigDecimal PESO_RENT_CONSERVADOR = new BigDecimal("0.2");
    public static final BigDecimal PESO_RISCO_CONSERVADOR = new BigDecimal("0.2");

    // ===========================
    // PESOS - MODERADO
    // ===========================
    public static final BigDecimal PESO_LIQ_MODERADO = new BigDecimal("0.4");
    public static final BigDecimal PESO_RENT_MODERADO = new BigDecimal("0.4");
    public static final BigDecimal PESO_RISCO_MODERADO = new BigDecimal("0.2");

    // ===========================
    // PESOS - AGRESSIVO
    // ===========================
    public static final BigDecimal PESO_LIQ_AGRESSIVO = new BigDecimal("0.2");
    public static final BigDecimal PESO_RENT_AGRESSIVO = new BigDecimal("0.6");
    public static final BigDecimal PESO_RISCO_AGRESSIVO = new BigDecimal("0.2");

    public static final String PERFIL_CONSERVADOR_PRODUTO = "BAIXO";
    public static final String PERFIL_MODERADO_PRODUTO = "MEDIO";
    public static final String PERFIL_AGRESSIVO_PRODUTO = "ALTO";

}
