package br.com.seuempresa.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.ProdutoRiscoRegra;
import br.com.caixaverso.invest.infra.repository.ProdutoRiscoRegraRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
class ProdutoRiscoRegraRepositoryTest {

    @Test
    void testListarTodas() {
        ProdutoRiscoRegraRepository repo = Mockito.spy(new ProdutoRiscoRegraRepository());

        doReturn(List.of(new ProdutoRiscoRegra())).when(repo).listAll();

        List<ProdutoRiscoRegra> result = repo.listarTodas();

        assertEquals(1, result.size());
        verify(repo).listAll();
    }
}
