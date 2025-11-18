package br.com.caixaverso.invest.infra.util;

public final class RiscoMapper {

    private RiscoMapper() {
    }

    public static String mapearRiscoHumano(String riscoTecnico) {
        if (riscoTecnico == null) return "Desconhecido";

        return switch (riscoTecnico.toUpperCase()) {
            case "CONSERVADOR" -> "Baixo";
            case "MODERADO"    -> "Médio";
            case "AGRESSIVO"   -> "Alto";
            default -> "Desconhecido";
        };
    }
}
