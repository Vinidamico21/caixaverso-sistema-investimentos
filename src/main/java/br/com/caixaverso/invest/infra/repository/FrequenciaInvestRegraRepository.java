package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.infra.persistence.entity.FrequenciaInvestRegra;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FrequenciaInvestRegraRepository implements PanacheRepository<FrequenciaInvestRegra> {

    @CacheResult(cacheName = "frequencia-invest-regras")
    public List<FrequenciaInvestRegra> listarTodas() {
        return listAll();
    }
}