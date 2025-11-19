package br.com.caixaverso.invest.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "ProdutoRiscoRegra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoRiscoRegra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rentabilidade_min", nullable = false, precision = 10, scale = 4)
    private BigDecimal faixaMin;

    @Column(name = "rentabilidade_max", nullable = false, precision = 10, scale = 4)
    private BigDecimal faixaMax;

    @Column(name = "risco", nullable = false, length = 20)
    private String risco;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoRiscoRegra that = (ProdutoRiscoRegra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProdutoRiscoRegra{" +
                "id=" + id +
                ", faixaMin=" + faixaMin +
                ", faixaMax=" + faixaMax +
                ", risco='" + risco + '\'' +
                '}';
    }
}