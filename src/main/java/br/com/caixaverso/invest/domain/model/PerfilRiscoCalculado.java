package br.com.caixaverso.invest.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerfilRiscoCalculado {

    private Long clienteId;
    private String codigo;
    private BigDecimal score;
    private String descricao;

}

