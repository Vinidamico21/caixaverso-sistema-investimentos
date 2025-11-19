package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.infra.persistence.entity.Investimento;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testa os métodos de conveniência do InvestimentoRepository,
 * garantindo que delegam corretamente para PanacheRepository.find(...)
 */
@ExtendWith(MockitoExtension.class)
class InvestimentoRepositoryTest {

    @Test
    void deveBuscarPorClienteId() {
        Long clienteId = 10L;

        InvestimentoRepository repo = spy(new InvestimentoRepository());
        PanacheQuery<Investimento> query = mock(PanacheQuery.class);

        List<Investimento> esperado = List.of(new Investimento(), new Investimento());

        // stubbing - isso gera uma interação com o spy
        doReturn(query).when(repo).find("cliente.id = ?1", clienteId);
        when(query.list()).thenReturn(esperado);

        List<Investimento> resultado = repo.findByClienteId(clienteId);

        assertSame(esperado, resultado);

        verify(repo, times(1)).find("cliente.id = ?1", clienteId);
        verify(query, times(1)).list();
        // Garante que só houve interação esperada com o query
        verifyNoMoreInteractions(query);
    }

    @Test
    void deveBuscarPorClienteIdEPeriodo() {
        Long clienteId = 20L;
        LocalDate dataInicio = LocalDate.of(2024, 1, 1);

        InvestimentoRepository repo = spy(new InvestimentoRepository());
        PanacheQuery<Investimento> query = mock(PanacheQuery.class);

        List<Investimento> esperado = List.of(new Investimento());

        // find("cliente.id = ?1 and dataAporte >= ?2", clienteId, dataInicio.atStartOfDay())
        doReturn(query).when(repo).find(
                eq("cliente.id = ?1 and dataAporte >= ?2"),
                eq(clienteId),
                any(LocalDateTime.class)
        );
        when(query.list()).thenReturn(esperado);

        List<Investimento> resultado = repo.findByClienteIdAndPeriodo(clienteId, dataInicio);

        assertSame(esperado, resultado);

        verify(repo, times(1)).find(
                eq("cliente.id = ?1 and dataAporte >= ?2"),
                eq(clienteId),
                any(LocalDateTime.class)
        );
        verify(query, times(1)).list();
        // Não valida mais interações no spy, só no query
        verifyNoMoreInteractions(query);
    }

    @Test
    void deveBuscarPorClienteIdEStatus() {
        Long clienteId = 30L;
        String status = "ATIVO";

        InvestimentoRepository repo = spy(new InvestimentoRepository());
        PanacheQuery<Investimento> query = mock(PanacheQuery.class);

        List<Investimento> esperado = List.of(new Investimento(), new Investimento(), new Investimento());

        // find("cliente.id = ?1 and status = ?2", clienteId, status)
        doReturn(query).when(repo).find(
                "cliente.id = ?1 and status = ?2",
                clienteId,
                status
        );
        when(query.list()).thenReturn(esperado);

        List<Investimento> resultado = repo.findByClienteIdAndStatus(clienteId, status);

        assertSame(esperado, resultado);

        verify(repo, times(1)).find(
                "cliente.id = ?1 and status = ?2",
                clienteId,
                status
        );
        verify(query, times(1)).list();
        // Idem: apenas o query
        verifyNoMoreInteractions(query);
    }
}
