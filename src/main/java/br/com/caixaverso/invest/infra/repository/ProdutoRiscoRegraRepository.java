package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.infra.persistence.entity.ProdutoRiscoRegra;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProdutoRiscoRegraRepository implements PanacheRepository<ProdutoRiscoRegra> {

    @CacheResult(cacheName = "produto-risco-regras")
    public List<ProdutoRiscoRegra> listarTodas() {
        return listAll();
    }
}
