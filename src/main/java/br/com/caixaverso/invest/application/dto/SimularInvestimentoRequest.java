package br.com.caixaverso.invest.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimularInvestimentoRequest {

    @NotNull
    private Long clienteId;

    @NotNull
    @Positive
    private BigDecimal valor;

    @NotNull
    @Positive
    private Integer prazoMeses;

    @NotNull
    private String tipoProduto;
}
