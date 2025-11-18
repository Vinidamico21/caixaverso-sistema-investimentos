package br.com.caixaverso.invest.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "perfil_preferencia_regra")
@Getter
@Setter
public class PreferenciaRegra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_preferencia")
    private String tipoPreferencia;

    private Integer pontuacao;
}
