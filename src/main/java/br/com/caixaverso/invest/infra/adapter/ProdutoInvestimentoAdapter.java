package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import br.com.caixaverso.invest.domain.port.ProdutoInvestimentoPort;
import br.com.caixaverso.invest.infra.repository.ProdutoInvestimentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProdutoInvestimentoAdapter implements ProdutoInvestimentoPort {

    @Inject
    ProdutoInvestimentoRepository produtoRepository;

    @Override
    public List<ProdutoInvestimento> findAll() {
        return produtoRepository.listAll();
    }

    @Override
    public List<ProdutoInvestimento> findByTipo(String tipo) {
        return produtoRepository.list("tipo", tipo);
    }
}
