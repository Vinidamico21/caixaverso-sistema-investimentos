package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.infra.persistence.entity.TelemetriaRegistro;

public interface TelemetriaPort {
    TelemetriaRegistro salvar(TelemetriaRegistro registro);
}
