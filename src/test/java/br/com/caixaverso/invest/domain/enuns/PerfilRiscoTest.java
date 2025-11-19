package br.com.caixaverso.invest.domain.enuns;

import br.com.caixaverso.invest.domain.enums.PerfilRisco;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class PerfilRiscoTest {

    @Test
    @DisplayName("Deve retornar CONSERVADOR quando valor for 'CONSERVADOR'")
    void from_DeveRetornarConservador_QuandoValorForConservador() {
        PerfilRisco resultado = PerfilRisco.from("CONSERVADOR");
        assertEquals(PerfilRisco.CONSERVADOR, resultado);
    }

    @Test
    @DisplayName("Deve retornar MODERADO quando valor for 'MODERADO'")
    void from_DeveRetornarModerado_QuandoValorForModerado() {
        PerfilRisco resultado = PerfilRisco.from("MODERADO");
        assertEquals(PerfilRisco.MODERADO, resultado);
    }

    @Test
    @DisplayName("Deve retornar AGRESSIVO quando valor for 'AGGRESSIVO'")
    void from_DeveRetornarAgressivo_QuandoValorForAgressivo() {
        PerfilRisco resultado = PerfilRisco.from("AGRESSIVO");
        assertEquals(PerfilRisco.AGRESSIVO, resultado);
    }

    @Test
    @DisplayName("Deve converter para maiúsculas quando valor for em minúsculas")
    void from_DeveConverterMaiusculas_QuandoValorForMinusculas() {
        PerfilRisco resultado = PerfilRisco.from("conservador");
        assertEquals(PerfilRisco.CONSERVADOR, resultado);
    }

    @Test
    @DisplayName("Deve converter para maiúsculas quando valor for misto")
    void from_DeveConverterMaiusculas_QuandoValorForMisto() {
        PerfilRisco resultado = PerfilRisco.from("Moderado");
        assertEquals(PerfilRisco.MODERADO, resultado);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Deve retornar DESCONHECIDO quando valor for nulo ou vazio")
    void from_DeveRetornarDesconhecido_QuandoValorForNuloOuVazio(String valor) {
        PerfilRisco resultado = PerfilRisco.from(valor);
        assertEquals(PerfilRisco.DESCONHECIDO, resultado);
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALIDO", "TESTE", "XYZ", " "})
    @DisplayName("Deve retornar DESCONHECIDO quando valor for inválido")
    void from_DeveRetornarDesconhecido_QuandoValorForInvalido(String valorInvalido) {
        PerfilRisco resultado = PerfilRisco.from(valorInvalido);
        assertEquals(PerfilRisco.DESCONHECIDO, resultado);
    }

    @Test
    @DisplayName("Deve retornar DESCONHECIDO quando valor for string vazia")
    void from_DeveRetornarDesconhecido_QuandoValorForStringVazia() {
        PerfilRisco resultado = PerfilRisco.from("");
        assertEquals(PerfilRisco.DESCONHECIDO, resultado);
    }

    @Test
    @DisplayName("Deve retornar DESCONHECIDO quando valor for string com espaços")
    void from_DeveRetornarDesconhecido_QuandoValorForStringComEspacos() {
        PerfilRisco resultado = PerfilRisco.from("   ");
        assertEquals(PerfilRisco.DESCONHECIDO, resultado);
    }

    @Test
    @DisplayName("Deve retornar valores da enumeração corretamente")
    void values_DeveRetornarTodosOsValoresDaEnum() {
        PerfilRisco[] valores = PerfilRisco.values();

        assertEquals(4, valores.length);
        assertArrayEquals(new PerfilRisco[]{
                PerfilRisco.CONSERVADOR,
                PerfilRisco.MODERADO,
                PerfilRisco.AGRESSIVO,
                PerfilRisco.DESCONHECIDO
        }, valores);
    }
}