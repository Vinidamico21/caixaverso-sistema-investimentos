package br.com.caixaverso.invest.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoValidadoDTO {

    private Long id;
    private String nome;
    private String tipo;
    private BigDecimal rentabilidade;
    private String risco;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoValidadoDTO that = (ProdutoValidadoDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(nome, that.nome) &&
                Objects.equals(tipo, that.tipo) &&
                (rentabilidade == that.rentabilidade ||
                        (rentabilidade != null && that.rentabilidade != null &&
                                rentabilidade.compareTo(that.rentabilidade) == 0)) &&
                Objects.equals(risco, that.risco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, tipo,
                rentabilidade != null ? rentabilidade.stripTrailingZeros() : null,
                risco);
    }

    @Override
    public String toString() {
        return "ProdutoValidadoDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                ", rentabilidade=" + rentabilidade +
                ", risco='" + risco + '\'' +
                '}';
    }
}