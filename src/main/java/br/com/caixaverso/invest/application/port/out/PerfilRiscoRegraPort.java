package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.domain.model.PerfilRiscoRegra;

import java.util.Optional;

public interface PerfilRiscoRegraPort {
    Optional<PerfilRiscoRegra> buscarRegraPorScore(int score);
}

