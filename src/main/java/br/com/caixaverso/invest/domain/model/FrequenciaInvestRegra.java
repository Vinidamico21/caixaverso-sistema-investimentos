package br.com.caixaverso.invest.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "perfil_freq_invest_regra")
@Getter @Setter
public class FrequenciaInvestRegra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantidade_min")
    private Integer quantidadeMin;

    @Column(name = "quantidade_max")
    private Integer quantidadeMax;

    private Integer pontuacao;
}