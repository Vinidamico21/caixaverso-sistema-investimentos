package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testa os métodos de conveniência do ProdutoInvestimentoRepository,
 * garantindo que delegam corretamente para listAll() e list("tipo", tipo).
 */
@ExtendWith(MockitoExtension.class)
class ProdutoInvestimentoRepositoryTest {

    @Test
    void deveListarTodosOsProdutosUsandoListAll() {
        // spy da classe real, para interceptar chamadas a listAll()
        ProdutoInvestimentoRepository repo = spy(new ProdutoInvestimentoRepository());

        List<ProdutoInvestimento> esperado = List.of(
                new ProdutoInvestimento(),
                new ProdutoInvestimento()
        );

        // quando listAll() for chamado pelo método, devolve nossa lista fake
        doReturn(esperado).when(repo).listAll();

        // método sob teste
        List<ProdutoInvestimento> resultado = repo.listarTodos();

        // garante que é a mesma lista
        assertSame(esperado, resultado);

        // valida que delegou corretamente
        verify(repo, times(1)).listAll();
        // não usamos verifyNoMoreInteractions(repo) por causa do stubbing com spy
    }

    @Test
    void deveListarPorTipoUsandoQueryCorreta() {
        ProdutoInvestimentoRepository repo = spy(new ProdutoInvestimentoRepository());

        String tipo = "RENDA_FIXA";
        List<ProdutoInvestimento> esperado = List.of(
                new ProdutoInvestimento()
        );

        // PanacheRepository.list(String, Object...) retorna List, não PanacheQuery
        doReturn(esperado).when(repo).list(eq("tipo"), eq(tipo));

        List<ProdutoInvestimento> resultado = repo.listarPorTipo(tipo);

        assertSame(esperado, resultado);

        verify(repo, times(1)).list("tipo", tipo);
        // idem aqui: sem verifyNoMoreInteractions(repo)
    }
}
