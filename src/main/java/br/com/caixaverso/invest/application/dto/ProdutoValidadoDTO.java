package br.com.caixaverso.invest.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoValidadoDTO {

    private Long id;
    private String nome;
    private String tipo;
    private BigDecimal rentabilidade;
    private String risco;
}
