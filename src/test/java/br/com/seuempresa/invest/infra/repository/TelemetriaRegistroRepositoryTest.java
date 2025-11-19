package br.com.seuempresa.invest.infra.repository;

import br.com.caixaverso.invest.infra.repository.TelemetriaRegistroRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TelemetriaRegistroRepositoryTest {

    @Test
    void testInstance() {
        TelemetriaRegistroRepository repo = new TelemetriaRegistroRepository();
        assertNotNull(repo);
    }
}