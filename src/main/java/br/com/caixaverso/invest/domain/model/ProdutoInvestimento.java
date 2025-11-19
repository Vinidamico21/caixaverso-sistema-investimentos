package br.com.caixaverso.invest.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ProdutoInvestimento", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoInvestimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column(name = "risco", nullable = false, length = 20)
    private String risco;

    @Column(name = "taxa_anual", nullable = false, precision = 7, scale = 4)
    private BigDecimal taxaAnual;

    @Column(name = "liquidez", nullable = false, length = 30)
    private String liquidez;

    // No SQL está como NULLABLE – você marcou nullable = false,
    // isso só vai dar problema se houver registros com NULL.
    @Column(name = "prazo_min_meses", nullable = true)
    private Integer prazoMinMeses;

    @Column(name = "prazo_max_meses", nullable = true)
    private Integer prazoMaxMeses;

    @Column(name = "valor_minimo", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorMinimo;

    // No SQL é nullable, aqui ajusto também para true por segurança
    @Column(name = "valor_maximo", nullable = true, precision = 18, scale = 2)
    private BigDecimal valorMaximo;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdutoInvestimento that = (ProdutoInvestimento) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }

    @Override
    public String toString() {
        return "ProdutoInvestimento{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                ", risco='" + risco + '\'' +
                ", taxaAnual=" + taxaAnual +
                ", liquidez='" + liquidez + '\'' +
                ", prazoMinMeses=" + prazoMinMeses +
                ", prazoMaxMeses=" + prazoMaxMeses +
                ", valorMinimo=" + valorMinimo +
                ", valorMaximo=" + valorMaximo +
                ", ativo=" + ativo +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}
