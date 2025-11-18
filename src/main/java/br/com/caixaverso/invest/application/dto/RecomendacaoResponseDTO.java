package br.com.caixaverso.invest.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecomendacaoResponseDTO {

    private Long clienteId;
    private String perfilRisco;
    private String scoreRisco;
    private List<ProdutoRecomendadoDTO> produtos;
}
