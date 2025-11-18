package br.com.caixaverso.invest.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PerfilRiscoRegra")
@Getter
@Setter
public class PerfilRiscoRegra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "score_min", nullable = false)
    private Integer scoreMin;

    @Column(name = "score_max", nullable = false)
    private Integer scoreMax;

    @Column(name = "perfil", nullable = false)
    private String perfil;
}
