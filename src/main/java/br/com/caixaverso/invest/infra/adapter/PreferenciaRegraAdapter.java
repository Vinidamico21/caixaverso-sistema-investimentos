package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.PreferenciaRegra;
import br.com.caixaverso.invest.application.port.out.PreferenciaRegraPort;
import br.com.caixaverso.invest.infra.repository.PreferenciaRegraRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PreferenciaRegraAdapter implements PreferenciaRegraPort {

    @Inject
    PreferenciaRegraRepository repo;

    @Override
    public int buscarPontuacao(String tipoPreferencia) {
        PreferenciaRegra regra = repo.buscarPorTipo(tipoPreferencia);
        return regra != null ? regra.getPontuacao() : 0;
    }
}