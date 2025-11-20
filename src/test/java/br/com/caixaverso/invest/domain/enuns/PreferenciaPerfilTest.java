package br.com.caixaverso.invest.domain.enuns;

import br.com.caixaverso.invest.domain.enums.PreferenciaPerfil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class PreferenciaPerfilTest {

    @Test
    @DisplayName("Deve conter todos os valores esperados na enumeração")
    void values_DeveConterTodosOsValoresEsperados() {
        PreferenciaPerfil[] valores = PreferenciaPerfil.values();

        assertEquals(3, valores.length);
        assertArrayEquals(new PreferenciaPerfil[]{
                PreferenciaPerfil.LIQUIDEZ,
                PreferenciaPerfil.RENTABILIDADE,
                PreferenciaPerfil.EMPATE
        }, valores);
    }

    @Test
    @DisplayName("Deve retornar LIQUIDEZ quando acessado por nome")
    void valueOf_DeveRetornarLiquidez_QuandoAcessadoPorNome() {
        PreferenciaPerfil resultado = PreferenciaPerfil.valueOf("LIQUIDEZ");
        assertEquals(PreferenciaPerfil.LIQUIDEZ, resultado);
    }

    @Test
    @DisplayName("Deve retornar RENTABILIDADE quando acessado por nome")
    void valueOf_DeveRetornarRentabilidade_QuandoAcessadoPorNome() {
        PreferenciaPerfil resultado = PreferenciaPerfil.valueOf("RENTABILIDADE");
        assertEquals(PreferenciaPerfil.RENTABILIDADE, resultado);
    }

    @Test
    @DisplayName("Deve retornar EMPATE quando acessado por nome")
    void valueOf_DeveRetornarEmpate_QuandoAcessadoPorNome() {
        PreferenciaPerfil resultado = PreferenciaPerfil.valueOf("EMPATE");
        assertEquals(PreferenciaPerfil.EMPATE, resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar acessar valor inexistente")
    void valueOf_DeveLancarExcecao_QuandoValorInexistente() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PreferenciaPerfil.valueOf("INEXISTENTE");
        });

        assertEquals("No enum constant br.com.caixaverso.invest.domain.enums.PreferenciaPerfil.INEXISTENTE",
                exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar a ordem correta dos valores")
    void ordinal_DeveRetornarOrdemCorreta() {
        assertEquals(0, PreferenciaPerfil.LIQUIDEZ.ordinal());
        assertEquals(1, PreferenciaPerfil.RENTABILIDADE.ordinal());
        assertEquals(2, PreferenciaPerfil.EMPATE.ordinal());
    }

    @Test
    @DisplayName("Deve comparar corretamente os valores usando compareTo")
    void compareTo_DeveCompararCorretamente() {
        assertTrue(PreferenciaPerfil.LIQUIDEZ.compareTo(PreferenciaPerfil.RENTABILIDADE) < 0);
        assertTrue(PreferenciaPerfil.RENTABILIDADE.compareTo(PreferenciaPerfil.EMPATE) < 0);
        assertTrue(PreferenciaPerfil.EMPATE.compareTo(PreferenciaPerfil.LIQUIDEZ) > 0);
        assertEquals(0, PreferenciaPerfil.LIQUIDEZ.compareTo(PreferenciaPerfil.LIQUIDEZ));
    }

    @Test
    @DisplayName("Deve retornar nome correto usando name()")
    void name_DeveRetornarNomeCorreto() {
        assertEquals("LIQUIDEZ", PreferenciaPerfil.LIQUIDEZ.name());
        assertEquals("RENTABILIDADE", PreferenciaPerfil.RENTABILIDADE.name());
        assertEquals("EMPATE", PreferenciaPerfil.EMPATE.name());
    }

    @Test
    @DisplayName("Deve retornar string representativa usando toString()")
    void toString_DeveRetornarStringCorreta() {
        assertEquals("LIQUIDEZ", PreferenciaPerfil.LIQUIDEZ.toString());
        assertEquals("RENTABILIDADE", PreferenciaPerfil.RENTABILIDADE.toString());
        assertEquals("EMPATE", PreferenciaPerfil.EMPATE.toString());
    }

    @Test
    @DisplayName("Deve verificar igualdade corretamente")
    void equals_DeveVerificarIgualdadeCorreta() {
        assertEquals(PreferenciaPerfil.LIQUIDEZ, PreferenciaPerfil.LIQUIDEZ);
        assertNotEquals(PreferenciaPerfil.LIQUIDEZ, PreferenciaPerfil.RENTABILIDADE);
        assertNotEquals(PreferenciaPerfil.EMPATE, PreferenciaPerfil.LIQUIDEZ);
    }

    @Test
    @DisplayName("Deve funcionar corretamente em estruturas de dados")
    void enum_DeveFuncionarEmEstruturasDeDados() {
        // Teste para verificar que a enumeração funciona em switch statements
        String resultado = switch (PreferenciaPerfil.LIQUIDEZ) {
            case LIQUIDEZ -> "Liquidez selecionada";
            case RENTABILIDADE -> "Rentabilidade selecionada";
            case EMPATE -> "Empate selecionado";
        };

        assertEquals("Liquidez selecionada", resultado);
    }
}