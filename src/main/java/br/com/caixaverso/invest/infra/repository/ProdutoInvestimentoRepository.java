package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProdutoInvestimentoRepository implements PanacheRepository<ProdutoInvestimento> {

    @CacheResult(cacheName = "produtos-todos")
    public List<ProdutoInvestimento> listarTodos() {
        return listAll();
    }

    @CacheResult(cacheName = "produtos-por-tipo")
    public List<ProdutoInvestimento> listarPorTipo(@CacheKey String tipo) {
        return list("tipo", tipo);
    }
}