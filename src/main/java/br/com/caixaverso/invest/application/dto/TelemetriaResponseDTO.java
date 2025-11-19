package br.com.caixaverso.invest.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class TelemetriaResponseDTO {

    private List<TelemetriaServicoDTO> servicos;
    private TelemetriaPeriodoDTO periodo;

    @Override
    public String toString() {
        return "TelemetriaResponseDTO{" +
                "servicos=" + servicos +
                ", periodo=" + periodo +
                '}';
    }
}