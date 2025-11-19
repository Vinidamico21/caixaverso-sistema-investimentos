package br.com.caixaverso.invest.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulacaoResumoDTO {

    private Long id;
    private Long clienteId;
    private String produto;
    private BigDecimal valorInvestido;
    private BigDecimal valorFinal;
    private Integer prazoMeses;
    private OffsetDateTime dataSimulacao;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimulacaoResumoDTO that = (SimulacaoResumoDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(clienteId, that.clienteId) &&
                Objects.equals(produto, that.produto) &&
                Objects.equals(valorInvestido, that.valorInvestido) &&
                Objects.equals(valorFinal, that.valorFinal) &&
                Objects.equals(prazoMeses, that.prazoMeses) &&
                Objects.equals(dataSimulacao, that.dataSimulacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clienteId, produto, valorInvestido, valorFinal, prazoMeses, dataSimulacao);
    }

    @Override
    public String toString() {
        return "SimulacaoResumoDTO{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", produto='" + produto + '\'' +
                ", valorInvestido=" + valorInvestido +
                ", valorFinal=" + valorFinal +
                ", prazoMeses=" + prazoMeses +
                ", dataSimulacao=" + dataSimulacao +
                '}';
    }
}