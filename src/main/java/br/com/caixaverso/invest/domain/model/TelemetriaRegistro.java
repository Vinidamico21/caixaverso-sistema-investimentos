package br.com.caixaverso.invest.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "TelemetriaRegistro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class TelemetriaRegistro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String endpoint;

    @Column(name = "metodo_http", length = 10, nullable = false)
    private String metodoHttp;

    @Column(nullable = false)
    private Boolean sucesso;

    @Column(name = "status_http")
    private Integer statusHttp;

    @Column(name = "duracao_ms")
    private Integer duracaoMs;

    @CreationTimestamp
    @Column(name = "data_registro", updatable = false)
    private LocalDateTime dataRegistro;
}
