package br.com.caixaverso.invest.application.dto.response;

import br.com.caixaverso.invest.application.dto.ProdutoValidadoDTO;
import br.com.caixaverso.invest.application.dto.ResultadoSimulacaoDTO;
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
