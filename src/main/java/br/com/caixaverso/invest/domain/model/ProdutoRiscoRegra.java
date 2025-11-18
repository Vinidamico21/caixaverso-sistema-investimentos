package br.com.caixaverso.invest.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ProdutoRiscoRegra")
@Getter @Setter
public class ProdutoRiscoRegra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rentabilidade_min", nullable = false, precision = 10, scale = 4)
    private BigDecimal faixaMin;

    @Column(name = "rentabilidade_max", precision = 10, scale = 4)
    private BigDecimal faixaMax;

    @Column(nullable = false, length = 20)
    private String risco;
}
