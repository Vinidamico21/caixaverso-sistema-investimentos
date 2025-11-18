package br.com.caixaverso.invest.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelemetriaResponseDTO {

    private List<TelemetriaServicoDTO> servicos;
    private TelemetriaPeriodoDTO periodo;
}
