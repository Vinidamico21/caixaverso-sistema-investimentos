package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.ProdutoRiscoRegra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoRiscoRegraRepositoryTest {

    @Test
    void deveListarTodasAsRegrasUsandoListAll() {
        ProdutoRiscoRegraRepository repo = spy(new ProdutoRiscoRegraRepository());

        List<ProdutoRiscoRegra> esperado = List.of(
                new ProdutoRiscoRegra(),
                new ProdutoRiscoRegra()
        );

        // quando listAll() for chamado pelo método, devolve nossa lista fake
        doReturn(esperado).when(repo).listAll();

        // método sob teste
        List<ProdutoRiscoRegra> resultado = repo.listarTodas();

        // mesma lista
        assertSame(esperado, resultado);

        // garante que o método delegou para listAll()
        verify(repo, times(1)).listAll();
        // NÃO usamos verifyNoMoreInteractions(repo) porque a chamada ao próprio
        // método sob teste (listarTodas) também é uma interação do spy.
    }
}
