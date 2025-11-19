package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.domain.model.Cliente;
import br.com.caixaverso.invest.application.port.out.ClientePort;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ClienteService {

    @Inject
    ClientePort clientePort;

    public Cliente buscarCliente(Long id) {
        return clientePort.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public boolean existe(Long id) {
        return clientePort.findById(id).isPresent();
    }
}
