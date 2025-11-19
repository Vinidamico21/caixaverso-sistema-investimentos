package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.SimulacaoPorProdutoDiaDTO;

import java.util.List;

public interface AgruparSimulacoesPorProdutoDiaUseCase {

    List<SimulacaoPorProdutoDiaDTO> agrupamentoPorProdutoDia();

}

