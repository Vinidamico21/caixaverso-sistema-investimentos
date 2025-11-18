package br.com.caixaverso.invest.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilRiscoResponseDTO {

    private Long clienteId;
    private String perfil;
    private String pontuacao;
    private String descricao;
}
