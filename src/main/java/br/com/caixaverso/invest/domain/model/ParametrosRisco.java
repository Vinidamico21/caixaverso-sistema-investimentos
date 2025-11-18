package br.com.caixaverso.invest.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ParametrosRisco")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParametrosRisco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;
}
