package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.infra.persistence.entity.FrequenciaSimulacaoRegra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FrequenciaSimulacaoRegraRepositoryTest {

    @Test
    void deveListarTodasAsRegrasDeSimulacaoUsandoListAll() {
        FrequenciaSimulacaoRegraRepository repo = spy(new FrequenciaSimulacaoRegraRepository());

        List<FrequenciaSimulacaoRegra> esperado = List.of(
                new FrequenciaSimulacaoRegra(),
                new FrequenciaSimulacaoRegra()
        );

        // stub do listAll()
        doReturn(esperado).when(repo).listAll();

        // método sob teste
        List<FrequenciaSimulacaoRegra> resultado = repo.listarTodas();

        // mesma lista
        assertSame(esperado, resultado);

        // verifica delegação
        verify(repo, times(1)).listAll();
        // sem verifyNoMoreInteractions(repo) pelo mesmo motivo do teste anterior
    }
}
