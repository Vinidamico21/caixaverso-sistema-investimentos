package br.com.caixaverso.invest.domain.enums;

public enum RiscoProduto {
    BAIXO,
    MEDIO,
    ALTO;

    public static RiscoProduto from(String valor) {
        if (valor == null) return BAIXO;
        try {
            return valueOf(valor.toUpperCase());
        } catch (Exception e) {
            return BAIXO;
        }
    }
}
