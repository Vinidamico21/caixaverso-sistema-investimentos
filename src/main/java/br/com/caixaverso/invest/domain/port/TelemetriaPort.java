package br.com.caixaverso.invest.domain.port;

import br.com.caixaverso.invest.domain.model.TelemetriaRegistro;

public interface TelemetriaPort {
    TelemetriaRegistro salvar(TelemetriaRegistro registro);
}
