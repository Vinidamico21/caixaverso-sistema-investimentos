package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.FrequenciaSimulacaoRegra;
import br.com.caixaverso.invest.domain.port.FrequenciaSimulacaoRegraPort;
import br.com.caixaverso.invest.infra.repository.FrequenciaSimulacaoRegraRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FrequenciaSimulacaoRegraAdapter implements FrequenciaSimulacaoRegraPort {

    @Inject
    FrequenciaSimulacaoRegraRepository repo;

    @Override
    public int buscarPontuacao(int quantidade) {
        return repo.findAll().stream()
                .filter(r -> quantidade >= r.getQuantidadeMin() &&
                        (r.getQuantidadeMax() == null || quantidade <= r.getQuantidadeMax()))
                .map(FrequenciaSimulacaoRegra::getPontuacao)
                .findFirst()
                .orElse(0);
    }
}

