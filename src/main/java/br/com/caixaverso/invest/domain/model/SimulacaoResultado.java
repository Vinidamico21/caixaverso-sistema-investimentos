package br.com.caixaverso.invest.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@AllArgsConstructor
@ToString
public class SimulacaoResultado {

    private final ProdutoInvestimento produto;
    private final BigDecimal valorFinal;
    private final Integer prazoMeses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimulacaoResultado that = (SimulacaoResultado) o;
        return Objects.equals(produto, that.produto) &&
                Objects.equals(valorFinal, that.valorFinal) &&
                Objects.equals(prazoMeses, that.prazoMeses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produto, valorFinal, prazoMeses);
    }
}