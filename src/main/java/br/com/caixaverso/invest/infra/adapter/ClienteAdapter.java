package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.Cliente;
import br.com.caixaverso.invest.application.port.out.ClientePort;
import br.com.caixaverso.invest.infra.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class ClienteAdapter implements ClientePort {

    @Inject
    ClienteRepository clienteRepository;

    @Override
    public Optional<Cliente> findById(Long id) {
        // Panache já tem findByIdOptional
        return clienteRepository.findByIdOptional(id);
    }
}
