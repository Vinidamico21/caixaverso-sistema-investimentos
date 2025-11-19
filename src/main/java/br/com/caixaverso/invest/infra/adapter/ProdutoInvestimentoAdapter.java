package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import br.com.caixaverso.invest.application.port.out.ProdutoInvestimentoPort;
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
        return produtoRepository.listarTodos();
    }

    @Override
    public List<ProdutoInvestimento> findByTipo(String tipo) {
        return produtoRepository.listarPorTipo(tipo);
    }
}