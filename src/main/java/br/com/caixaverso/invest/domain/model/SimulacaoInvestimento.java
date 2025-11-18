package br.com.caixaverso.invest.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "SimulacaoInvestimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulacaoInvestimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private ProdutoInvestimento produto;

    @Column(name = "valor_aplicado", precision = 18, scale = 2, nullable = false)
    private BigDecimal valorAplicado;

    @Column(name = "prazo_meses", nullable = false)
    private Integer prazoMeses;

    @Column(name = "valor_final", precision = 18, scale = 2, nullable = false)
    private BigDecimal valorFinal;

    @Column(name = "data_simulacao", nullable = false)
    private LocalDateTime dataSimulacao;

    @Column(name = "data_simulacao_dia", insertable = false, updatable = false)
    private LocalDate dataSimulacaoDia;

    @Column(name = "perfil_risco_calculado", length = 20)
    private String perfilRiscoCalculado;

    @Column(name = "score_risco", precision = 7, scale = 4)
    private BigDecimal scoreRisco;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
}
