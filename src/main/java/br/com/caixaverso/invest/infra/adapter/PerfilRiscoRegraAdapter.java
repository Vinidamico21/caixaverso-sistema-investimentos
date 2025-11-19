package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.PerfilRiscoRegra;
import br.com.caixaverso.invest.application.port.out.PerfilRiscoRegraPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class PerfilRiscoRegraAdapter implements PerfilRiscoRegraPort {

    @Inject
    PerfilRiscoRegraRepository repo;

    @Override
    public Optional<PerfilRiscoRegra> buscarRegraPorScore(int score) {
        return repo.buscarPorScore(score);
    }
}