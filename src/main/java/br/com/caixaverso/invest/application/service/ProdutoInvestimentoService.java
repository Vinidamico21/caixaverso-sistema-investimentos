package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import br.com.caixaverso.invest.domain.port.ProdutoInvestimentoPort;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProdutoInvestimentoService {

    @Inject
    ProdutoInvestimentoPort produtoPort;

    public List<ProdutoInvestimento> listarProdutos() {
        return produtoPort.findAll();
    }

    public List<ProdutoInvestimento> listarPorTipo(String tipo) {
        return produtoPort.findByTipo(tipo);
    }
}
