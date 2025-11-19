package br.com.caixaverso.invest.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class InvestimentoResponseDTO {
    private Long id;

    @JsonProperty("produto")
    private String tipo;

    @JsonProperty("valorAplicado")
    private BigDecimal valor;

    private BigDecimal rentabilidade;

    @JsonProperty("dataAplicacao")
    private LocalDate data;
}