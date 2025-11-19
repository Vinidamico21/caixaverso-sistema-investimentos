package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.infra.persistence.entity.FrequenciaInvestRegra;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrequenciaInvestRegraTest {

    @Test
    void testNoArgsConstructorEGettersSetters() {
        // Arrange & Act (Criação e definição de valores)
        FrequenciaInvestRegra regra = new FrequenciaInvestRegra();
        regra.setQuantidadeMin(5);
        regra.setQuantidadeMax(10);
        regra.setPontuacao(5);

        // Assert (Verificação)
        assertEquals(5, regra.getQuantidadeMin());
        assertEquals(10, regra.getQuantidadeMax());
        assertEquals(5, regra.getPontuacao());
        // O ID é gerado pelo banco, então deve ser nulo ao criar uma nova instância
        assertNull(regra.getId());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange (Criação de dois objetos idênticos e um diferente)
        FrequenciaInvestRegra regra1 = new FrequenciaInvestRegra();
        regra1.setQuantidadeMin(5);
        regra1.setQuantidadeMax(10);
        regra1.setPontuacao(5);

        FrequenciaInvestRegra regra2 = new FrequenciaInvestRegra();
        regra2.setQuantidadeMin(5);
        regra2.setQuantidadeMax(10);
        regra2.setPontuacao(5);

        FrequenciaInvestRegra regra3 = new FrequenciaInvestRegra();
        regra3.setQuantidadeMin(11);
        regra3.setQuantidadeMax(20);
        regra3.setPontuacao(10);

        // Assert (Verificação da igualdade e hashCode)
        // Objetos com os mesmos valores de atributos devem ser iguais
        assertEquals(regra1, regra2);
        assertEquals(regra1.hashCode(), regra2.hashCode());

        // Objetos com valores diferentes não devem ser iguais
        assertNotEquals(regra1, regra3);
        assertNotEquals(regra1.hashCode(), regra3.hashCode());

        // Testes de robustez
        assertNotEquals(regra1, null);
        assertNotEquals(regra1, "um objeto de outra classe");
        assertEquals(regra1, regra1); // Reflexividade: um objeto é igual a si mesmo
    }

    @Test
    void testToString() {
        // Arrange (Criação de um objeto com dados)
        FrequenciaInvestRegra regra = new FrequenciaInvestRegra();
        regra.setQuantidadeMin(1);
        regra.setQuantidadeMax(4);
        regra.setPontuacao(2);

        // Act (Ação)
        String resultado = regra.toString();

        // Assert (Verificação do formato da string gerada pelo Lombok)
        // O @ToString padrão do Lombok usa parênteses, não chaves.
        assertTrue(resultado.contains("FrequenciaInvestRegra("));
        assertTrue(resultado.contains("quantidadeMin=1"));
        assertTrue(resultado.contains("quantidadeMax=4"));
        assertTrue(resultado.contains("pontuacao=2"));
    }
}