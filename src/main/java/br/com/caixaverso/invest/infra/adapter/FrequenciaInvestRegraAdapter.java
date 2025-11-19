package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.infra.persistence.entity.FrequenciaInvestRegra;
import br.com.caixaverso.invest.application.port.out.FrequenciaInvestRegraPort;
import br.com.caixaverso.invest.infra.repository.FrequenciaInvestRegraRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FrequenciaInvestRegraAdapter implements FrequenciaInvestRegraPort {

    @Inject
    FrequenciaInvestRegraRepository repo;

    @Override
    public int buscarPontuacao(int quantidade) {
        return repo.listarTodas().stream()
                .filter(r -> quantidade >= r.getQuantidadeMin() &&
                        (r.getQuantidadeMax() == null || quantidade <= r.getQuantidadeMax()))
                .map(FrequenciaInvestRegra::getPontuacao)
                .findFirst()
                .orElse(0);
    }
}