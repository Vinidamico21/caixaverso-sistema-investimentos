package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.request.SimularInvestimentoRequest;
import br.com.caixaverso.invest.application.dto.response.SimularInvestimentoResponse;

public interface SimularInvestimentoUseCase {

    SimularInvestimentoResponse executarSimulacao(SimularInvestimentoRequest request);

}