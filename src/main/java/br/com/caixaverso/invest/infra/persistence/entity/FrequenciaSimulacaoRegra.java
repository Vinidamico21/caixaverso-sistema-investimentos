package br.com.caixaverso.invest.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "perfil_freq_simulacao_regra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrequenciaSimulacaoRegra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantidade_min", nullable = false)
    private Integer quantidadeMin;

    @Column(name = "quantidade_max", nullable = false)
    private Integer quantidadeMax;

    @Column(name = "pontuacao", nullable = false)
    private Integer pontuacao;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrequenciaSimulacaoRegra that = (FrequenciaSimulacaoRegra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FrequenciaSimulacaoRegra{" +
                "id=" + id +
                ", quantidadeMin=" + quantidadeMin +
                ", quantidadeMax=" + quantidadeMax +
                ", pontuacao=" + pontuacao +
                '}';
    }
}