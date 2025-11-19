package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.infra.persistence.entity.FrequenciaInvestRegra;
import br.com.caixaverso.invest.infra.repository.FrequenciaInvestRegraRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FrequenciaInvestRegraAdapterTest {

    @InjectMocks
    FrequenciaInvestRegraAdapter adapter;

    @Mock
    FrequenciaInvestRegraRepository repo;

    // helper para criar regras
    private FrequenciaInvestRegra regra(Integer min, Integer max, int pontuacao) {
        FrequenciaInvestRegra r = new FrequenciaInvestRegra();
        r.setQuantidadeMin(min);
        r.setQuantidadeMax(max);
        r.setPontuacao(pontuacao);
        return r;
    }

    // -------------------------------------------------
    // Deve classificar corretamente quando há regras com min/max
    // -------------------------------------------------
    @Test
    void deveRetornarPontuacaoConformeFaixas() {
        when(repo.listarTodas()).thenReturn(List.of(
                // 1 a 3 → 10 pontos
                regra(1, 3, 10),
                // 4 a 6 → 20 pontos
                regra(4, 6, 20),
                // >= 7 (max null) → 30 pontos
                regra(7, null, 30)
        ));

        // dentro da primeira faixa
        assertEquals(10, adapter.buscarPontuacao(2));

        // limite inferior e superior da segunda faixa
        assertEquals(20, adapter.buscarPontuacao(4));
        assertEquals(20, adapter.buscarPontuacao(6));

        // acima de 7, pega faixa com max = null
        assertEquals(30, adapter.buscarPontuacao(10));

        // repo chamado uma vez por invocação
        verify(repo, times(4)).listarTodas();
    }

    // -------------------------------------------------
    // Quando nenhuma regra satisfaz o filtro → retorna 0
    // -------------------------------------------------
    @Test
    void deveRetornarZeroQuandoNenhumaRegraForEncontrada() {
        when(repo.listarTodas()).thenReturn(List.of(
                // só faixa 5–10
                regra(5, 10, 50)
        ));

        // quantidade abaixo da menor faixa → não casa com nenhuma regra
        int pontuacao = adapter.buscarPontuacao(2);

        assertEquals(0, pontuacao);
        verify(repo, times(1)).listarTodas();
    }
}

