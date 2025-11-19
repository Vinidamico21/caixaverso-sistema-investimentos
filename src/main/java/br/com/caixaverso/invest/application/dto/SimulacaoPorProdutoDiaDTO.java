package br.com.caixaverso.invest.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulacaoPorProdutoDiaDTO {

    private String produto;
    private LocalDate data;
    private Long quantidadeSimulacoes;
    private BigDecimal mediaValorFinal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimulacaoPorProdutoDiaDTO that = (SimulacaoPorProdutoDiaDTO) o;
        return Objects.equals(produto, that.produto) &&
                Objects.equals(data, that.data) &&
                Objects.equals(quantidadeSimulacoes, that.quantidadeSimulacoes) &&
                ((mediaValorFinal == null && that.mediaValorFinal == null) ||
                        (mediaValorFinal != null && that.mediaValorFinal != null &&
                                mediaValorFinal.compareTo(that.mediaValorFinal) == 0));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                produto,
                data,
                quantidadeSimulacoes,
                mediaValorFinal == null ? null : mediaValorFinal.stripTrailingZeros()
        );
    }

    @Override
    public String toString() {
        return "SimulacaoPorProdutoDiaDTO{" +
                "produto='" + produto + '\'' +
                ", data=" + data +
                ", quantidadeSimulacoes=" + quantidadeSimulacoes +
                ", mediaValorFinal=" + mediaValorFinal +
                '}';
    }
}