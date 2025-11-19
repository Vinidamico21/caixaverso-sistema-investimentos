package br.com.caixaverso.invest.domain.enums;

public enum PerfilRisco {
    CONSERVADOR,
    MODERADO,
    AGRESSIVO,
    DESCONHECIDO;

    public static PerfilRisco from(String valor) {
        if (valor == null) return DESCONHECIDO;
        try {
            return valueOf(valor.toUpperCase());
        } catch (Exception e) {
            return DESCONHECIDO;
        }
    }
}
