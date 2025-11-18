package br.com.caixaverso.invest.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "PesosRecomendacao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"perfil_risco", "tipo_produto"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PesosRecomendacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "perfil_risco", length = 20, nullable = false)
    private String perfilRisco;

    @Column(name = "tipo_produto", length = 30, nullable = false)
    private String tipoProduto;

    @Column(nullable = false, precision = 7, scale = 4)
    private BigDecimal peso;
}