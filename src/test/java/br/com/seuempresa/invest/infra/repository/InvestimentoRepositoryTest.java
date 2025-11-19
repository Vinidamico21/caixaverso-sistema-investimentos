package br.com.seuempresa.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.Investimento;
import br.com.caixaverso.invest.infra.repository.InvestimentoRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
class InvestimentoRepositoryTest {

    @InjectMock
    InvestimentoRepository repository;

    @Test
    void testFindByClienteId() {
        // Arrange (Preparação)
        Long clienteId = 10L;
        Investimento investimentoEsperado = new Investimento();
        List<Investimento> investimentosEsperados = List.of(investimentoEsperado);

        // CORREÇÃO: Mockamos o método final diretamente.
        // É muito mais simples do que mockar o PanacheQuery.
        when(repository.findByClienteId(clienteId)).thenReturn(investimentosEsperados);

        // Act (Ação)
        List<Investimento> result = repository.findByClienteId(clienteId);

        // Assert (Verificação)
        assertEquals(1, result.size());
        assertEquals(investimentosEsperados, result);
    }

    @Test
    void testFindByClienteIdAndStatus() {
        // Arrange (Preparação)
        Long clienteId = 10L;
        String status = "ATIVO";
        Investimento investimentoEsperado = new Investimento();
        List<Investimento> investimentosEsperados = List.of(investimentoEsperado);

        // CORREÇÃO: Mockamos o método final diretamente.
        when(repository.findByClienteIdAndStatus(clienteId, status)).thenReturn(investimentosEsperados);

        // Act (Ação)
        List<Investimento> result = repository.findByClienteIdAndStatus(clienteId, status);

        // Assert (Verificação)
        assertEquals(1, result.size());
        assertEquals(investimentosEsperados, result);
    }
}