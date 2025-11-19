package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.infra.persistence.entity.TelemetriaRegistro;
import br.com.caixaverso.invest.infra.repository.TelemetriaRegistroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelemetriaAdapterTest {

    @InjectMocks
    TelemetriaAdapter adapter;

    @Mock
    TelemetriaRegistroRepository repo;

    @Test
    void devePersistirRegistroERetornarMesmoObjeto() {
        TelemetriaRegistro registro = new TelemetriaRegistro(); // ou mock(TelemetriaRegistro.class)

        TelemetriaRegistro retorno = adapter.salvar(registro);

        assertSame(registro, retorno);
        verify(repo, times(1)).persist(registro);
        verifyNoMoreInteractions(repo);
    }
}