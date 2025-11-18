package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.ProdutoRiscoRegra;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProdutoRiscoRegraRepository implements PanacheRepository<ProdutoRiscoRegra> {
}
