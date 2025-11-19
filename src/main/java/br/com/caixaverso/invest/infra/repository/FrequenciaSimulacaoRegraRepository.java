package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.FrequenciaSimulacaoRegra;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FrequenciaSimulacaoRegraRepository implements PanacheRepository<FrequenciaSimulacaoRegra> {

    @CacheResult(cacheName = "frequencia-sim-regras")
    public List<FrequenciaSimulacaoRegra> listarTodas() {
        return listAll();
    }
}