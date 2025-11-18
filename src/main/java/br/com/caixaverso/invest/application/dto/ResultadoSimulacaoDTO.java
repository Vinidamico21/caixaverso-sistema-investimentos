package br.com.caixaverso.invest.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultadoSimulacaoDTO {

    private BigDecimal valorFinal;
    private BigDecimal rentabilidadeEfetiva;
    private Integer prazoMeses;
}
