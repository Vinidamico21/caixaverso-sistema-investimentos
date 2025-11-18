package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.TelemetriaRegistro;
import br.com.caixaverso.invest.domain.port.TelemetriaPort;
import br.com.caixaverso.invest.infra.repository.TelemetriaRegistroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TelemetriaAdapter implements TelemetriaPort {

    @Inject
    TelemetriaRegistroRepository telemetriaRepository;

    @Override
    public TelemetriaRegistro salvar(TelemetriaRegistro registro) {
        telemetriaRepository.persist(registro);
        return registro;
    }
}
