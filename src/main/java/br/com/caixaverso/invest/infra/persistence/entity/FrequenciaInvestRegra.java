package br.com.caixaverso.invest.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "perfil_freq_invest_regra")
@Getter @Setter
@EqualsAndHashCode
@ToString
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