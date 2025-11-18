package br.com.caixaverso.invest.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulacaoPorProdutoDiaDTO {

    private String produto;
    private LocalDate data;
    private Long quantidadeSimulacoes;
    private BigDecimal mediaValorFinal;
}
