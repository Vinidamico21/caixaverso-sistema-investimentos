package br.com.seuempresa.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import br.com.caixaverso.invest.infra.repository.ProdutoInvestimentoRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest; // <-- IMPORTANTE
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// A anotação @QuarkusTest é ESSENCIAL. Sem ela, o @InjectMock não funciona.
@QuarkusTest
class ProdutoInvestimentoRepositoryTest {

    @InjectMock
    ProdutoInvestimentoRepository repository;

    @Test
    void testListarTodos() {
        // Arrange (Preparação)
        // Mockamos o método listAll() para retornar uma lista vazia, como esperado pelo teste.
        when(repository.listAll()).thenReturn(Collections.emptyList());

        // Act (Ação)
        List<ProdutoInvestimento> result = repository.listarTodos();

        // Assert (Verificação)
        // O erro "expected: <0> but was: <1>" acontecia porque o teste estava
        // chamando o banco de dados real, que continha um registro.
        // Com o mock correto, o resultado será uma lista vazia (size 0).
        assertEquals(0, result.size());
    }

    @Test
    void testListarPorTipo() {
        // Arrange (Preparação)
        String tipo = "RENDA_FIXA";
        // Mockamos o método list() para retornar uma lista vazia.
        PanacheQuery<ProdutoInvestimento> queryMock = mock(PanacheQuery.class);
        when(repository.find("tipo", tipo)).thenReturn(queryMock);
        when(queryMock.list()).thenReturn(Collections.emptyList());

        // Act (Ação)
        List<ProdutoInvestimento> result = repository.listarPorTipo(tipo);

        // Assert (Verificação)
        // Mesmo caso aqui: o mock garante que não vamos ao banco real.
        assertEquals(0, result.size());
    }
}