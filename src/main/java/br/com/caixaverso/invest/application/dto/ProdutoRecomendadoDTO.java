package br.com.caixaverso.invest.application.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoRecomendadoDTO {
    private Long id;
    private String nome;
    private String tipo;
    private BigDecimal rentabilidade;
    private String risco;

    @Override
    public String toString() {
        return "ProdutoRecomendadoDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                ", rentabilidade=" + rentabilidade +
                ", risco='" + risco + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoRecomendadoDTO that = (ProdutoRecomendadoDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(nome, that.nome) &&
                Objects.equals(tipo, that.tipo) &&
                Objects.equals(rentabilidade, that.rentabilidade) &&
                Objects.equals(risco, that.risco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, tipo, rentabilidade, risco);
    }
}