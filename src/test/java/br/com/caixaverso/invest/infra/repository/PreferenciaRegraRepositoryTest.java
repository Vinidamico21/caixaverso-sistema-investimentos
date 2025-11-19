package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.PreferenciaRegra;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreferenciaRegraRepositoryTest {

    @Test
    void deveBuscarPorTipoUtilizandoFindCorretamente() {
        String tipoPreferencia = "ALTA_LIQUIDEZ";

        PreferenciaRegraRepository repo = spy(new PreferenciaRegraRepository());
        PanacheQuery<PreferenciaRegra> query = mock(PanacheQuery.class);

        PreferenciaRegra esperado = new PreferenciaRegra();
        esperado.setTipoPreferencia(tipoPreferencia);

        // stub do find(...) retornando a query mockada
        doReturn(query).when(repo).find("tipoPreferencia", tipoPreferencia);
        when(query.firstResult()).thenReturn(esperado);

        // método sob teste
        PreferenciaRegra resultado = repo.buscarPorTipo(tipoPreferencia);

        // mesma instância
        assertSame(esperado, resultado);

        // verificação das interações importantes
        verify(repo, times(1)).find("tipoPreferencia", tipoPreferencia);
        verify(query, times(1)).firstResult();
        // aqui só garantimos que a query não teve mais nada além de firstResult()
        verifyNoMoreInteractions(query);
    }
}
