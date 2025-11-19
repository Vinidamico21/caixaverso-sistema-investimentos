package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.domain.model.Cliente;
import java.util.Optional;

public interface ClientePort {
    Optional<Cliente> findById(Long id);
}