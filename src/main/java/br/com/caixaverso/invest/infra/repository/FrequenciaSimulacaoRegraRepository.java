package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.FrequenciaSimulacaoRegra;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FrequenciaSimulacaoRegraRepository implements PanacheRepository<FrequenciaSimulacaoRegra> {
}

