package br.com.caixaverso.invest.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "perfil_preferencia_regra")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PreferenciaRegra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_preferencia")
    private String tipoPreferencia;

    private Integer pontuacao;
}
