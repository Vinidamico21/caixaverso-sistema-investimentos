package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import br.com.caixaverso.invest.infra.repository.ProdutoInvestimentoRepository;
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
class ProdutoInvestimentoAdapterTest {

    @InjectMocks
    ProdutoInvestimentoAdapter adapter;

    @Mock
    ProdutoInvestimentoRepository repo;

    private ProdutoInvestimento criarProduto(Long id, String codigo, String tipo) {
        return ProdutoInvestimento.builder()
                .id(id)
                .codigo(codigo)
                .nome("Produto " + codigo)
                .tipo(tipo)
                .risco("BAIXO")
                .taxaAnual(new BigDecimal("0.10"))
                .liquidez("D+1")
                .prazoMinMeses(6)
                .prazoMaxMeses(24)
                .valorMinimo(new BigDecimal("100.00"))
                .valorMaximo(new BigDecimal("10000.00"))
                .ativo(true)
                .build();
    }

    @Test
    void deveRetornarTodosOsProdutosAoChamarFindAll() {
        List<ProdutoInvestimento> mockList = List.of(
                criarProduto(1L, "CDB100", "RENDA_FIXA"),
                criarProduto(2L, "CDB200", "RENDA_FIXA")
        );

        when(repo.listarTodos()).thenReturn(mockList);

        List<ProdutoInvestimento> resultado = adapter.findAll();

        assertEquals(2, resultado.size());
        assertEquals(mockList, resultado);

        verify(repo, times(1)).listarTodos();
        verifyNoMoreInteractions(repo);
    }

    @Test
    void deveBuscarProdutosPorTipoAoChamarFindByTipo() {
        String tipo = "RENDA_VARIAVEL";

        List<ProdutoInvestimento> mockList = List.of(
                criarProduto(3L, "ACOES01", tipo),
                criarProduto(4L, "ACOES02", tipo)
        );

        when(repo.listarPorTipo(tipo)).thenReturn(mockList);

        List<ProdutoInvestimento> resultado = adapter.findByTipo(tipo);

        assertEquals(2, resultado.size());
        assertEquals(mockList, resultado);

        verify(repo, times(1)).listarPorTipo(tipo);
        verifyNoMoreInteractions(repo);
    }
}
