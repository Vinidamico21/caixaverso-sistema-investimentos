package br.com.caixaverso.invest.domain.model;

import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

public record SimulacaoResultado(ProdutoInvestimento produto, BigDecimal valorFinal, Integer prazoMeses) {

}