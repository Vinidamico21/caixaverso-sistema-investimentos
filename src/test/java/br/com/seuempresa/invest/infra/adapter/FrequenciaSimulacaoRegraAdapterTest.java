package br.com.seuempresa.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.FrequenciaSimulacaoRegra;
import br.com.caixaverso.invest.infra.adapter.FrequenciaSimulacaoRegraAdapter;
import br.com.caixaverso.invest.infra.repository.FrequenciaSimulacaoRegraRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject; // Import necessário para @Inject
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class FrequenciaSimulacaoRegraAdapterTest {

    @InjectMock
    FrequenciaSimulacaoRegraRepository repository;

    // CORREÇÃO: Injete o adaptador em vez de instanciá-lo manualmente.
    // O Quarkus injetará o mock do repositório automaticamente.
    @Inject
    FrequenciaSimulacaoRegraAdapter adapter;

    @Test
    void buscarPontuacao_comQuantidadeEmFaixaDefinida_deveRetornarPontuacaoCorreta() {
        // Arrange: A quantidade 8 corresponde à primeira regra.
        FrequenciaSimulacaoRegra regra1 = new FrequenciaSimulacaoRegra();
        regra1.setQuantidadeMin(5);
        regra1.setQuantidadeMax(10);
        regra1.setPontuacao(5);

        FrequenciaSimulacaoRegra regra2 = new FrequenciaSimulacaoRegra();
        regra2.setQuantidadeMin(11);
        regra2.setQuantidadeMax(20);
        regra2.setPontuacao(10);

        when(repository.listarTodas()).thenReturn(List.of(regra1, regra2));

        // Act
        int resultado = adapter.buscarPontuacao(8);

        // Assert: Deve retornar a pontuação da primeira regra correspondente (5).
        assertEquals(5, resultado);
        verify(repository).listarTodas();
    }

    @Test
    void buscarPontuacao_comQuantidadeEmFaixaAberta_deveRetornarPontuacaoCorreta() {
        // Arrange: A quantidade 25 corresponde à regra de faixa aberta.
        FrequenciaSimulacaoRegra regraBaixa = new FrequenciaSimulacaoRegra();
        regraBaixa.setQuantidadeMin(1);
        regraBaixa.setQuantidadeMax(10);
        regraBaixa.setPontuacao(3);

        FrequenciaSimulacaoRegra regraAlta = new FrequenciaSimulacaoRegra();
        regraAlta.setQuantidadeMin(20);
        regraAlta.setQuantidadeMax(null); // Faixa aberta
        regraAlta.setPontuacao(15);

        when(repository.listarTodas()).thenReturn(List.of(regraBaixa, regraAlta));

        // Act
        int resultado = adapter.buscarPontuacao(25);

        // Assert
        assertEquals(15, resultado);
        verify(repository).listarTodas();
    }

    @Test
    void buscarPontuacao_comQuantidadeForaDaFaixa_deveRetornarZero() {
        // Arrange: A quantidade 3 não corresponde a nenhuma das regras.
        FrequenciaSimulacaoRegra regra = new FrequenciaSimulacaoRegra();
        regra.setQuantidadeMin(5);
        regra.setQuantidadeMax(15);
        regra.setPontuacao(8);

        when(repository.listarTodas()).thenReturn(List.of(regra));

        // Act
        int resultado = adapter.buscarPontuacao(3);

        // Assert: Nenhuma regra corresponde, deve retornar o padrão 0.
        assertEquals(0, resultado);
        verify(repository).listarTodas();
    }

    @Test
    void buscarPontuacao_comListaDeRegrasVazia_deveRetornarZero() {
        // Arrange: O repositório não possui nenhuma regra.
        when(repository.listarTodas()).thenReturn(List.of());

        // Act
        int resultado = adapter.buscarPontuacao(10);

        // Assert: Com uma lista vazia, deve retornar o padrão 0.
        assertEquals(0, resultado);
        verify(repository).listarTodas();
    }
}