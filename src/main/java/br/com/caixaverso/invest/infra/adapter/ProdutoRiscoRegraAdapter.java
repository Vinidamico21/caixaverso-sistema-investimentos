package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.ProdutoRiscoRegra;
import br.com.caixaverso.invest.domain.port.ProdutoRiscoRegraPort;
import br.com.caixaverso.invest.infra.repository.ProdutoRiscoRegraRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;

@ApplicationScoped
public class ProdutoRiscoRegraAdapter implements ProdutoRiscoRegraPort {

    @Inject
    ProdutoRiscoRegraRepository repo;

    @Override
    public String classificar(BigDecimal taxaAnual) {

        if (taxaAnual == null)
            return "BAIXO";

        return repo.findAll().stream()
                .filter(r ->
                        taxaAnual.compareTo(r.getFaixaMin()) >= 0 &&
                                (r.getFaixaMax() == null || taxaAnual.compareTo(r.getFaixaMax()) <= 0)
                )
                .map(ProdutoRiscoRegra::getRisco)
                .findFirst()
                .orElse("BAIXO");
    }
}
