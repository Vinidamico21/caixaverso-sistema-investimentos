package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.FrequenciaSimulacaoRegra;
import br.com.caixaverso.invest.infra.repository.FrequenciaSimulacaoRegraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FrequenciaSimulacaoRegraAdapterTest {

    @Mock
    FrequenciaSimulacaoRegraRepository repository;

    // SUT
    FrequenciaSimulacaoRegraAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FrequenciaSimulacaoRegraAdapter();
        // campo é package-private, então podemos setar direto por estar no mesmo pacote
        adapter.repo = repository;
    }

    @Test
    void buscarPontuacao_quandoQuantidadeDentroDeFaixaFechada_deveRetornarPontuacaoDaRegra() {
        // quantidade 8 casa na regra1 (5–10)
        FrequenciaSimulacaoRegra regra1 = new FrequenciaSimulacaoRegra();
        regra1.setQuantidadeMin(5);
        regra1.setQuantidadeMax(10);
        regra1.setPontuacao(5);

        FrequenciaSimulacaoRegra regra2 = new FrequenciaSimulacaoRegra();
        regra2.setQuantidadeMin(11);
        regra2.setQuantidadeMax(20);
        regra2.setPontuacao(10);

        when(repository.listarTodas()).thenReturn(List.of(regra1, regra2));

        int resultado = adapter.buscarPontuacao(8);

        assertEquals(5, resultado);
        verify(repository).listarTodas();
    }

    @Test
    void buscarPontuacao_quandoQuantidadeDentroDeFaixaAberta_deveRetornarPontuacaoDaRegra() {
        // quantidade 25 casa na regraAlta (20–∞, max == null)
        FrequenciaSimulacaoRegra regraBaixa = new FrequenciaSimulacaoRegra();
        regraBaixa.setQuantidadeMin(1);
        regraBaixa.setQuantidadeMax(10);
        regraBaixa.setPontuacao(3);

        FrequenciaSimulacaoRegra regraAlta = new FrequenciaSimulacaoRegra();
        regraAlta.setQuantidadeMin(20);
        regraAlta.setQuantidadeMax(null); // faixa aberta
        regraAlta.setPontuacao(15);

        when(repository.listarTodas()).thenReturn(List.of(regraBaixa, regraAlta));

        int resultado = adapter.buscarPontuacao(25);

        assertEquals(15, resultado);
        verify(repository).listarTodas();
    }

    @Test
    void buscarPontuacao_quandoQuantidadeNaoEntraEmNenhumaFaixa_deveRetornarZero() {
        // quantidade 3 não cai na faixa 5–15
        FrequenciaSimulacaoRegra regra = new FrequenciaSimulacaoRegra();
        regra.setQuantidadeMin(5);
        regra.setQuantidadeMax(15);
        regra.setPontuacao(8);

        when(repository.listarTodas()).thenReturn(List.of(regra));

        int resultado = adapter.buscarPontuacao(3);

        assertEquals(0, resultado);
        verify(repository).listarTodas();
    }

    @Test
    void buscarPontuacao_quandoNaoExistemRegras_deveRetornarZero() {
        when(repository.listarTodas()).thenReturn(List.of());

        int resultado = adapter.buscarPontuacao(10);

        assertEquals(0, resultado);
        verify(repository).listarTodas();
    }
}
