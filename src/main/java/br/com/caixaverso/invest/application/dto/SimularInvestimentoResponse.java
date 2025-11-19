package br.com.caixaverso.invest.application.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class SimularInvestimentoResponse {
    private ProdutoValidadoDTO produtoValidado;
    private ResultadoSimulacaoDTO resultadoSimulacao;
    private OffsetDateTime dataSimulacao;
}
