package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.Investimento;
import br.com.caixaverso.invest.infra.repository.InvestimentoRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@QuarkusTest
class InvestimentoAdapterTest {

    @InjectMock
    InvestimentoRepository investimentoRepository;

    @Inject
    InvestimentoAdapter adapter;

    @Test
    void deveDelegarFindByClienteIdParaRepository() {
        Long clienteId = 10L;
        List<Investimento> esperado = List.of(new Investimento(), new Investimento());

        when(investimentoRepository.findByClienteId(clienteId)).thenReturn(esperado);

        List<Investimento> resultado = adapter.findByClienteId(clienteId);

        assertSame(esperado, resultado);
        verify(investimentoRepository, times(1)).findByClienteId(clienteId);
    }

    @Test
    void deveDelegarFindByClienteIdAndPeriodoParaRepository() {
        Long clienteId = 20L;
        LocalDate dataInicio = LocalDate.of(2024, 1, 1);
        List<Investimento> esperado = List.of(new Investimento());

        when(investimentoRepository.findByClienteIdAndPeriodo(clienteId, dataInicio))
                .thenReturn(esperado);

        List<Investimento> resultado = adapter.findByClienteIdAndPeriodo(clienteId, dataInicio);

        assertSame(esperado, resultado);
        verify(investimentoRepository, times(1))
                .findByClienteIdAndPeriodo(clienteId, dataInicio);
    }

    @Test
    void deveDelegarSaveParaRepositoryEPersistirInvestimento() {
        Investimento investimento = new Investimento();

        // persist é void, então usamos doNothing/verify
        doNothing().when(investimentoRepository).persist(investimento);

        Investimento resultado = adapter.save(investimento);

        assertSame(investimento, resultado);
        verify(investimentoRepository, times(1)).persist(investimento);
    }

    @Test
    void deveDelegarFindAllParaRepository() {
        List<Investimento> esperado = List.of(new Investimento(), new Investimento());

        when(investimentoRepository.listAll()).thenReturn(esperado);

        List<Investimento> resultado = adapter.findAll();

        assertSame(esperado, resultado);
        verify(investimentoRepository, times(1)).listAll();
    }
}
