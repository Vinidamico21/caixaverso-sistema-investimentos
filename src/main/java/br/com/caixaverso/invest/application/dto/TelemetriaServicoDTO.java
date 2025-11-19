package br.com.caixaverso.invest.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class TelemetriaServicoDTO {

    private String nome;
    private Long quantidadeChamadas;
    private Long mediaTempoRespostaMs;
}
