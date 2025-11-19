package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.infra.persistence.entity.FrequenciaSimulacaoRegra;
import br.com.caixaverso.invest.application.port.out.FrequenciaSimulacaoRegraPort;
import br.com.caixaverso.invest.infra.repository.FrequenciaSimulacaoRegraRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FrequenciaSimulacaoRegraAdapter implements FrequenciaSimulacaoRegraPort {

    @Inject
    FrequenciaSimulacaoRegraRepository repo;

    @Override
    public int buscarPontuacao(int quantidade) {
        return repo.listarTodas().stream()
                .filter(r -> quantidade >= r.getQuantidadeMin() &&
                        (r.getQuantidadeMax() == null || quantidade <= r.getQuantidadeMax()))
                .map(FrequenciaSimulacaoRegra::getPontuacao)
                .findFirst()
                .orElse(0);
    }
}