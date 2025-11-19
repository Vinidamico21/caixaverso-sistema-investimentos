package br.com.caixaverso.invest.domain.enuns;

import br.com.caixaverso.invest.domain.enums.RiscoProduto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class RiscoProdutoTest {

    @Test
    @DisplayName("Deve retornar BAIXO quando valor for 'BAIXO'")
    void from_DeveRetornarBaixo_QuandoValorForBaixo() {
        RiscoProduto resultado = RiscoProduto.from("BAIXO");
        assertEquals(RiscoProduto.BAIXO, resultado);
    }

    @Test
    @DisplayName("Deve retornar MEDIO quando valor for 'MEDIO'")
    void from_DeveRetornarMedio_QuandoValorForMedio() {
        RiscoProduto resultado = RiscoProduto.from("MEDIO");
        assertEquals(RiscoProduto.MEDIO, resultado);
    }

    @Test
    @DisplayName("Deve retornar ALTO quando valor for 'ALTO'")
    void from_DeveRetornarAlto_QuandoValorForAlto() {
        RiscoProduto resultado = RiscoProduto.from("ALTO");
        assertEquals(RiscoProduto.ALTO, resultado);
    }

    @Test
    @DisplayName("Deve converter para maiúsculas quando valor for em minúsculas")
    void from_DeveConverterMaiusculas_QuandoValorForMinusculas() {
        RiscoProduto resultado = RiscoProduto.from("baixo");
        assertEquals(RiscoProduto.BAIXO, resultado);
    }

    @Test
    @DisplayName("Deve converter para maiúsculas quando valor for misto")
    void from_DeveConverterMaiusculas_QuandoValorForMisto() {
        RiscoProduto resultado = RiscoProduto.from("MeDiO");
        assertEquals(RiscoProduto.MEDIO, resultado);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Deve retornar BAIXO quando valor for nulo ou vazio")
    void from_DeveRetornarBaixo_QuandoValorForNuloOuVazio(String valor) {
        RiscoProduto resultado = RiscoProduto.from(valor);
        assertEquals(RiscoProduto.BAIXO, resultado);
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALIDO", "TESTE", "XYZ", " ", "OUTRO"})
    @DisplayName("Deve retornar BAIXO quando valor for inválido")
    void from_DeveRetornarBaixo_QuandoValorForInvalido(String valorInvalido) {
        RiscoProduto resultado = RiscoProduto.from(valorInvalido);
        assertEquals(RiscoProduto.BAIXO, resultado);
    }

    @Test
    @DisplayName("Deve retornar BAIXO quando valor for string vazia")
    void from_DeveRetornarBaixo_QuandoValorForStringVazia() {
        RiscoProduto resultado = RiscoProduto.from("");
        assertEquals(RiscoProduto.BAIXO, resultado);
    }

    @Test
    @DisplayName("Deve retornar BAIXO quando valor for string com espaços")
    void from_DeveRetornarBaixo_QuandoValorForStringComEspacos() {
        RiscoProduto resultado = RiscoProduto.from("   ");
        assertEquals(RiscoProduto.BAIXO, resultado);
    }

    @Test
    @DisplayName("Deve retornar valores da enumeração corretamente")
    void values_DeveRetornarTodosOsValoresDaEnum() {
        RiscoProduto[] valores = RiscoProduto.values();

        assertEquals(3, valores.length);
        assertArrayEquals(new RiscoProduto[]{
                RiscoProduto.BAIXO,
                RiscoProduto.MEDIO,
                RiscoProduto.ALTO
        }, valores);
    }

    @Test
    @DisplayName("Deve retornar BAIXO como valor padrão para casos de erro")
    void from_DeveRetornarBaixoComoPadrao_EmCasoDeErro() {
        // Teste específico para garantir que BAIXO é o valor padrão
        RiscoProduto resultado1 = RiscoProduto.from(null);
        RiscoProduto resultado2 = RiscoProduto.from("QUALQUER_COISA");

        assertEquals(RiscoProduto.BAIXO, resultado1);
        assertEquals(RiscoProduto.BAIXO, resultado2);
    }

    @Test
    @DisplayName("Deve manter a ordem ordinal correta")
    void ordinal_DeveManterOrdemCorreta() {
        assertEquals(0, RiscoProduto.BAIXO.ordinal());
        assertEquals(1, RiscoProduto.MEDIO.ordinal());
        assertEquals(2, RiscoProduto.ALTO.ordinal());
    }

    @Test
    @DisplayName("Deve funcionar corretamente com valueOf padrão")
    void valueOf_DeveFuncionarCorretamente() {
        assertEquals(RiscoProduto.BAIXO, RiscoProduto.valueOf("BAIXO"));
        assertEquals(RiscoProduto.MEDIO, RiscoProduto.valueOf("MEDIO"));
        assertEquals(RiscoProduto.ALTO, RiscoProduto.valueOf("ALTO"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando valueOf receber valor inválido")
    void valueOf_DeveLancarExcecao_QuandoValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            RiscoProduto.valueOf("INEXISTENTE");
        });
    }

    @Test
    @DisplayName("Deve retornar nomes corretos")
    void name_DeveRetornarNomesCorretos() {
        assertEquals("BAIXO", RiscoProduto.BAIXO.name());
        assertEquals("MEDIO", RiscoProduto.MEDIO.name());
        assertEquals("ALTO", RiscoProduto.ALTO.name());
    }
}
