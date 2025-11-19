package br.com.seuempresa.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.FrequenciaInvestRegra;
import br.com.caixaverso.invest.infra.repository.FrequenciaInvestRegraRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FrequenciaInvestRegraRepositoryTest {

    @Test
    void testListarTodas() {
        FrequenciaInvestRegraRepository repo = Mockito.spy(new FrequenciaInvestRegraRepository());

        List<FrequenciaInvestRegra> fakeList = List.of(new FrequenciaInvestRegra());
        doReturn(fakeList).when(repo).listAll();

        List<FrequenciaInvestRegra> result = repo.listarTodas();

        assertEquals(1, result.size());
        verify(repo).listAll();
    }
}
