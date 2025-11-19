package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.ProdutoRiscoRegra;
import br.com.caixaverso.invest.infra.repository.ProdutoRiscoRegraRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoRiscoRegraAdapterTest {

    @InjectMocks
    ProdutoRiscoRegraAdapter adapter;

    @Mock
    ProdutoRiscoRegraRepository repo;

    // -------------------------------------------------
    // helper para criar regras de risco
    // -------------------------------------------------
    private ProdutoRiscoRegra regra(BigDecimal min, BigDecimal max, String risco) {
        ProdutoRiscoRegra r = new ProdutoRiscoRegra();
        r.setFaixaMin(min);
        r.setFaixaMax(max);
        r.setRisco(risco);
        return r;
    }

    // -------------------------------------------------
    // taxaAnual == null → retorna "BAIXO" e não consulta repo
    // -------------------------------------------------
    @Test
    void deveRetornarBaixoQuandoTaxaAnualForNula() {
        String risco = adapter.classificar(null);

        assertEquals("BAIXO", risco);
        verifyNoInteractions(repo);
    }

    // -------------------------------------------------
    // regras com faixaMin / faixaMax (incluindo faixaMax = null)
    // -------------------------------------------------
    @Test
    void deveClassificarConformeFaixasDeTaxa() {
        when(repo.listarTodas()).thenReturn(List.of(
                regra(new BigDecimal("0.00"), new BigDecimal("0.10"), "BAIXO"),
                regra(new BigDecimal("0.10"), new BigDecimal("0.20"), "MEDIO"),
                regra(new BigDecimal("0.20"), null,                     "ALTO") // sem limite superior
        ));

        // dentro da primeira faixa
        assertEquals("BAIXO", adapter.classificar(new BigDecimal("0.05")));

        // limite inferior da segunda faixa (0.10) e dentro dela
        assertEquals("BAIXO", adapter.classificar(new BigDecimal("0.10")));
        assertEquals("MEDIO", adapter.classificar(new BigDecimal("0.19")));

        // acima de 0.20 → pega regra com faixaMax = null
        assertEquals("ALTO", adapter.classificar(new BigDecimal("0.30")));

        verify(repo, times(4)).listarTodas();
    }

    // -------------------------------------------------
    // nenhuma regra compatível → orElse("BAIXO")
    // -------------------------------------------------
    @Test
    void deveRetornarBaixoQuandoNenhumaRegraForEncontrada() {
        when(repo.listarTodas()).thenReturn(List.of(
                regra(new BigDecimal("0.10"), new BigDecimal("0.20"), "MEDIO")
        ));

        // abaixo da menor faixa
        String risco = adapter.classificar(new BigDecimal("0.05"));

        assertEquals("BAIXO", risco);
        verify(repo, times(1)).listarTodas();
    }
}

