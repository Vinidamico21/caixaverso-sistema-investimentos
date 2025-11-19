package br.com.caixaverso.invest.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilRiscoResponseDTO {
    private Long clienteId;
    private String perfil;
    private String pontuacao;
    private String descricao;
}
