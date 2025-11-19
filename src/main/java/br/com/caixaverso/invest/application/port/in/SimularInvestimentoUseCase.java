package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.SimularInvestimentoRequest;
import br.com.caixaverso.invest.application.dto.SimularInvestimentoResponse;

public interface SimularInvestimentoUseCase {

    SimularInvestimentoResponse executarSimulacao(SimularInvestimentoRequest request);

}