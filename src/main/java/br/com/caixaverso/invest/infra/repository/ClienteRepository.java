package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.Cliente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {

    public Optional<Cliente> buscarPorDocumento(String documento) {
        return find("documento", documento).firstResultOptional();
    }

    public boolean existeDocumento(String documento) {
        return count("documento", documento) > 0;
    }
}
