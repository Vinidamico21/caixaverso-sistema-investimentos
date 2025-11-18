package br.com.caixaverso.invest.domain.port;

import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import java.util.List;

public interface ProdutoInvestimentoPort {
    List<ProdutoInvestimento> findAll();
    List<ProdutoInvestimento> findByTipo(String tipo);
}
