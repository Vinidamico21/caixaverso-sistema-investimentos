package br.com.caixaverso.invest.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp; 

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ProdutoInvestimento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProdutoInvestimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false, length = 20)
    private String risco;

    @Column(name = "taxa_anual", precision = 7, scale = 4, nullable = false)
    private BigDecimal taxaAnual;

    @Column(nullable = false, length = 30)
    private String liquidez;

    @Column(name = "prazo_min_meses")
    private Integer prazoMinMeses;

    @Column(name = "prazo_max_meses")
    private Integer prazoMaxMeses;

    @Column(name = "valor_minimo", precision = 18, scale = 2, nullable = false)
    private BigDecimal valorMinimo;

    @Column(name = "valor_maximo", precision = 18, scale = 2)
    private BigDecimal valorMaximo;

    @Column(nullable = false)
    private Boolean ativo;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
}
