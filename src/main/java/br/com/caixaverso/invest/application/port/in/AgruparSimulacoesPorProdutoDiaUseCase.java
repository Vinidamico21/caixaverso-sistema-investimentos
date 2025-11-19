package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.SimulacaoPorProdutoDiaDTO;

import br.com.caixaverso.invest.application.dto.response.PageResponse;

public interface AgruparSimulacoesPorProdutoDiaUseCase {
    PageResponse<SimulacaoPorProdutoDiaDTO> agrupamentoPorProdutoDia(int page, int size);
}