package br.com.seuempresa.invest.infra.repository;

import br.com.caixaverso.invest.infra.repository.SimulacaoInvestimentoRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SimulacaoInvestimentoRepositoryTest {

    @Test
    void testInstance() {
        SimulacaoInvestimentoRepository repo = new SimulacaoInvestimentoRepository();
        assertNotNull(repo);
    }
}

