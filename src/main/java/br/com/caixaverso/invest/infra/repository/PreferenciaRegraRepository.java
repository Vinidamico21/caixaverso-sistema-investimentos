package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.PreferenciaRegra;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PreferenciaRegraRepository implements PanacheRepository<PreferenciaRegra> {

    public PreferenciaRegra buscarPorTipo(String tipoPreferencia) {
        return find("tipoPreferencia", tipoPreferencia).firstResult();
    }
}
