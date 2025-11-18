package br.com.caixaverso.invest.domain.port;

import java.math.BigDecimal;

public interface ProdutoRiscoRegraPort {

    /**
     * Classifica o risco do produto com base na sua rentabilidade anual
     * comparando com as regras persistidas no banco.
     *
     * @param taxaAnual rentabilidade anual do produto
     * @return risco do produto (CONSERVADOR, MODERADO, AGRESSIVO)
     */
    String classificar(BigDecimal taxaAnual);
}
