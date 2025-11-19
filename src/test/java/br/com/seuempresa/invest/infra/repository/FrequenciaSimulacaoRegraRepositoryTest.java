package br.com.seuempresa.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.FrequenciaSimulacaoRegra;
import br.com.caixaverso.invest.infra.repository.FrequenciaSimulacaoRegraRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class FrequenciaSimulacaoRegraRepositoryTest {

    @Test
    void testListarTodas() {
        FrequenciaSimulacaoRegraRepository repo = Mockito.spy(new FrequenciaSimulacaoRegraRepository());

        List<FrequenciaSimulacaoRegra> fake = List.of(new FrequenciaSimulacaoRegra());
        doReturn(fake).when(repo).listAll();

        List<FrequenciaSimulacaoRegra> result = repo.listarTodas();

        assertEquals(1, result.size());
        verify(repo).listAll();
    }
}

