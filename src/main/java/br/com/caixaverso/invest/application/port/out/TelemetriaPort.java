package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.domain.model.TelemetriaRegistro;

public interface TelemetriaPort {
    TelemetriaRegistro salvar(TelemetriaRegistro registro);
}
