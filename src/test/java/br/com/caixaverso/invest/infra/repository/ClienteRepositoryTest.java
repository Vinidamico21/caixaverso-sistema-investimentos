package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.infra.persistence.entity.Cliente;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteRepositoryTest {

    @Test
    void testBuscarPorDocumento() {
        ClienteRepository repo = Mockito.spy(new ClienteRepository());

        PanacheQuery<Cliente> query = mock(PanacheQuery.class);

        doReturn(query).when(repo).find("documento", "123");
        when(query.firstResultOptional()).thenReturn(Optional.of(new Cliente()));

        Optional<Cliente> result = repo.buscarPorDocumento("123");

        assertTrue(result.isPresent());
        verify(repo).find("documento", "123");
    }

    @Test
    void testExisteDocumento() {
        ClienteRepository repo = Mockito.spy(new ClienteRepository());

        doReturn(2L).when(repo).count("documento", "999");

        boolean exists = repo.existeDocumento("999");

        assertTrue(exists);
        verify(repo).count("documento", "999");
    }
}
