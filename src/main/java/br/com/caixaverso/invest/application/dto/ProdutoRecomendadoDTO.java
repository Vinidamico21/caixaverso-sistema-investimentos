package br.com.caixaverso.invest.application.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoRecomendadoDTO {
    private Long id;
    private String nome;
    private String tipo;
    private BigDecimal rentabilidade;
    private String risco;
}