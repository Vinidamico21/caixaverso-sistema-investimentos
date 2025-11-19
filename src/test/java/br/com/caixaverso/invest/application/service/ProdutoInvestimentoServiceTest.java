package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.port.out.ProdutoInvestimentoPort;
import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ProdutoInvestimentoServiceTest {

    @InjectMock
    ProdutoInvestimentoPort produtoPort;

    @Inject
    ProdutoInvestimentoService service;

    private ProdutoInvestimento produto1;
    private ProdutoInvestimento produto2;

    @BeforeEach
    void setup() {
        produto1 = new ProdutoInvestimento();
        produto1.setId(1L);
        produto1.setNome("CDB 100%");
        produto1.setTipo("RENDA_FIXA");

        produto2 = new ProdutoInvestimento();
        produto2.setId(2L);
        produto2.setNome("Tesouro Selic");
        produto2.setTipo("RENDA_FIXA");
    }

    @Test
    void deveListarProdutosComSucesso() {
        when(produtoPort.findAll()).thenReturn(List.of(produto1, produto2));

        List<ProdutoInvestimento> result = service.listarProdutos();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("CDB 100%", result.get(0).getNome());
        verify(produtoPort, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemProdutos() {
        when(produtoPort.findAll()).thenReturn(List.of());

        List<ProdutoInvestimento> result = service.listarProdutos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(produtoPort, times(1)).findAll();
    }

    @Test
    void deveListarProdutosPorTipoComSucesso() {
        when(produtoPort.findByTipo("RENDA_FIXA")).thenReturn(List.of(produto1, produto2));

        List<ProdutoInvestimento> result = service.listarPorTipo("RENDA_FIXA");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("RENDA_FIXA", result.get(0).getTipo());
        verify(produtoPort, times(1)).findByTipo("RENDA_FIXA");
    }
}