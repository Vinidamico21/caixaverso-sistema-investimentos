package br.com.seuempresa.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.PreferenciaRegra;
import br.com.caixaverso.invest.infra.repository.PreferenciaRegraRepository;
import io.quarkus.test.TestTransaction; // 1. Importe a anotação
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PreferenciaRegraRepositoryTest {

    @Inject
    PreferenciaRegraRepository repository;

    @Test
    @TestTransaction // 2. Adicione a anotação aqui
    void testBuscarPorTipo() {
        // Arrange (Preparação)
        PreferenciaRegra regra = new PreferenciaRegra();
        regra.setTipoPreferencia("CDB");
        regra.setPontuacao(5);

        // Persiste a entidade no banco de dados de teste
        repository.persist(regra);
        // O flush não é estritamente necessário aqui, mas garante que o comando foi enviado ao BD
        repository.getEntityManager().flush();

        // Act (Ação)
        PreferenciaRegra result = repository.buscarPorTipo("CDB");

        // Assert (Verificação)
        assertNotNull(result);
        assertEquals("CDB", result.getTipoPreferencia());
        assertEquals(5, result.getPontuacao());
    }
}