package br.com.caixaverso.invest.application.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelemetriaPeriodoDTO {

    private LocalDate inicio;
    private LocalDate fim;
}
