package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.infra.persistence.entity.PreferenciaRegra;
import br.com.caixaverso.invest.infra.repository.PreferenciaRegraRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para o {@link PreferenciaRegraAdapter}.
 * Verifica se a lógica de busca de pontuação funciona corretamente,
 * cobrindo os cenários de regra encontrada e não encontrada.
 */
@ExtendWith(MockitoExtension.class)
class PreferenciaRegraAdapterTest {

    @Mock
    private PreferenciaRegraRepository repo; // Mock da dependência

    @InjectMocks
    private PreferenciaRegraAdapter preferenciaRegraAdapter; // Classe sendo testada

    @Test
    @DisplayName("Deve retornar a pontuação correta quando uma regra for encontrada para o tipo de preferência")
    void deveRetornarPontuacaoQuandoRegraForEncontrada() {
        // Organização (Arrange)
        String tipoPreferencia = "CONSERVADOR";
        int pontuacaoEsperada = 10;

        // Criando o objeto que o repositório mockado irá retornar
        PreferenciaRegra regraMock = new PreferenciaRegra();
        regraMock.setPontuacao(pontuacaoEsperada);

        // "Ensina" o mock a retornar `regraMock` quando o método buscarPorTipo for chamado
        when(repo.buscarPorTipo(tipoPreferencia)).thenReturn(regraMock);

        // Ação (Act)
        int resultado = preferenciaRegraAdapter.buscarPontuacao(tipoPreferencia);

        // Verificação (Assert)
        assertEquals(pontuacaoEsperada, resultado, "A pontuação retornada deveria ser " + pontuacaoEsperada);
        // (Opcional, mas bom) Verifica se o método do repositório foi chamado
        verify(repo).buscarPorTipo(tipoPreferencia);
    }

    @Test
    @DisplayName("Deve retornar zero quando nenhuma regra for encontrada para o tipo de preferência")
    void deveRetornarZeroQuandoRegraNaoForEncontrada() {
        // Organização (Arrange)
        String tipoPreferenciaInexistente = "TIPO_INEXISTENTE";
        int pontuacaoEsperada = 0;

        // "Ensina" o mock a retornar null quando o método buscarPorTipo for chamado
        when(repo.buscarPorTipo(tipoPreferenciaInexistente)).thenReturn(null);

        // Ação (Act)
        int resultado = preferenciaRegraAdapter.buscarPontuacao(tipoPreferenciaInexistente);

        // Verificação (Assert)
        assertEquals(pontuacaoEsperada, resultado, "A pontuação deveria ser 0 quando nenhuma regra é encontrada");
        // (Opcional, mas bom) Verifica se o método do repositório foi chamado
        verify(repo).buscarPorTipo(tipoPreferenciaInexistente);
    }
}