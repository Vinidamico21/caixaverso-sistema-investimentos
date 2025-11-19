package br.com.caixaverso.invest.application.port.out;

import java.math.BigDecimal;

public interface ProdutoRiscoRegraPort {

    String classificar(BigDecimal taxaAnual);
}
