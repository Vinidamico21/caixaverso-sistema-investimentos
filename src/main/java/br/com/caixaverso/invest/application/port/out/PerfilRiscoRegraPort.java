package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.infra.persistence.entity.PerfilRiscoRegra;

import java.util.Optional;

public interface PerfilRiscoRegraPort {
    Optional<PerfilRiscoRegra> buscarRegraPorScore(int score);
}

