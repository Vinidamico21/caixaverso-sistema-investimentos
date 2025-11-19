package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.PreferenciaRegra;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PreferenciaRegraRepository implements PanacheRepository<PreferenciaRegra> {

    @CacheResult(cacheName = "preferencia-regra-por-tipo")
    public PreferenciaRegra buscarPorTipo(@CacheKey String tipoPreferencia) {
        return find("tipoPreferencia", tipoPreferencia).firstResult();
    }
}