package br.com.caixaverso.invest.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SimulacaoResultado {
    private ProdutoInvestimento produto;
    private BigDecimal valorFinal;
    private int prazoMeses;
}
